import fetch.gitlab.GitlabSchedule
import filter.CarAvailability
import filter.CarName
import filter.StationFilter
import filter.StationNames
import kotlinx.serialization.json.Json
import notification.sendWebhook
import parse.PipelineSchedule

private val WEBHOOK_URL: String = System.getenv("WEBHOOK_URL").also {
  requireNotNull(it) { "WEBHOOK_URL environment variable not set" }
}

private val PREFERRED_STATIONS: String = System.getenv("PREFERRED_STATIONS").also {
  requireNotNull(it) { "PREFERRED_STATIONS environment variable not set" }
}

private val READ_SCHEDULES_TOKEN: String = System.getenv("READ_SCHEDULES_TOKEN").also {
  requireNotNull(it) { "READ_SCHEDULES_TOKEN environment variable not set" }
}

private val CI_PROJECT_ID: String = System.getenv("CI_PROJECT_ID").also {
  requireNotNull(it) { "CI_PROJECT_ID environment variable not set" }
}

private val CI_PIPELINE_ID: String = System.getenv("CI_PIPELINE_ID").also {
  requireNotNull(it) { "CI_PIPELINE_ID environment variable not set" }
}

// private val USERNAME: String = System.getenv("USERNAME").also {
//  requireNotNull(it) { "USERNAME environment variable not set" }
// }
//
// private val PASSWORD: String = System.getenv("PASSWORD").also {
//  requireNotNull(it) { "PASSWORD environment variable not set" }
// }

suspend fun main() {
  val reservationGrid =
    System.`in`
      .readBytes()
      .decodeToString()
      .let {
        Json.decodeFromString(ReservationGrid.serializer(), it)
      }

  reservationGrid
    .filteredStations()
    .forEach { station ->
      sendWebhook(
        WEBHOOK_URL,
        "Date: ${reservationGrid.bookingStart} - ${reservationGrid.bookingEnd}\nStation: ${station.name}\n" +
          "Cars: ${station.cars.joinToString { it.name }}"
      )
    }
}

private fun ReservationGrid.filteredStations(): List<ReservationGrid.Station> =
  prepareFilters()
    .also { println("Filters: $it") }
    .fold(stations) { stations, filter ->
      filter.filter(stations)
    }

private fun prepareFilters() =
  GitlabSchedule(readSchedulesToken = READ_SCHEDULES_TOKEN, ciProjectId = CI_PROJECT_ID, ciPipelineId = CI_PIPELINE_ID)
    .current()
    ?.description
    ?.parseOptions()
    ?.mapToFilters()
    ?.plus(
      listOf(
        CarAvailability(availability = true),
        StationNames(names = PREFERRED_STATIONS.split(","))
      )
    )
    ?: throw IllegalArgumentException("No pipeline schedule found")

private fun String.parseOptions(): List<PipelineSchedule.Option> =
  PipelineSchedule.parse(this)

private fun List<PipelineSchedule.Option>.mapToFilters(): List<StationFilter> =
  map {
    when (it) {
      is PipelineSchedule.StationNames -> StationNames(names = it.names, inverse = it.inverse)
      is PipelineSchedule.CarName -> CarName(name = it.name)
      else -> throw IllegalArgumentException("Unknown option: $it")
    }
  }
