import fetch.GitlabSchedule
import filter.CarAvailability
import filter.CarName
import filter.StationNames
import kotlinx.serialization.json.Json
import notification.sendWebhook
import parse.PipelineSchedule

private val WEBHOOK_URL: String = System.getenv("WEBHOOK_URL").also {
  require(it != null) { "WEBHOOK_URL environment variable not set" }
}

private val PREFERRED_STATIONS: String = System.getenv("PREFERRED_STATIONS").also {
  require(it != null) { "PREFERRED_STATIONS environment variable not set" }
}

private val READ_SCHEDULES_TOKEN: String = System.getenv("READ_SCHEDULES_TOKEN").also {
  require(it != null) { "READ_SCHEDULES_TOKEN environment variable not set" }
}

private val CI_PROJECT_ID: String = System.getenv("CI_PROJECT_ID").also {
  require(it != null) { "CI_PROJECT_ID environment variable not set" }
}

private val CI_PIPELINE_ID: String = System.getenv("CI_PIPELINE_ID").also {
  require(it != null) { "CI_PIPELINE_ID environment variable not set" }
}

suspend fun main() {
  val reservationGrid =
    generateSequence { readlnOrNull() }
      .joinToString(separator = "")
      .let { Json.decodeFromString(ReservationGrid.serializer(), it) }

  prepareFilters()
    .also { println("Filters: $it") }
    .fold(reservationGrid.stations) { stations, filter ->
      filter.filter(stations)
    }.forEach { station ->
      sendWebhook(
        WEBHOOK_URL,
        "**Date**: ${reservationGrid.bookingStart} - ${reservationGrid.bookingEnd}\n**Station**: ${station.name}\n" +
          "**Cars**: ${station.cars.joinToString { it.name }}"
      )
    }
}

private fun prepareFilters() =
  GitlabSchedule(
    readSchedulesToken = READ_SCHEDULES_TOKEN,
    ciProjectId = CI_PROJECT_ID,
    ciPipelineId = CI_PIPELINE_ID
  ).current()
    ?.let {
      PipelineSchedule.parse(it.description)
    }?.map { option ->
      when (option) {
        is PipelineSchedule.StationNames -> StationNames(names = option.names, inverse = option.inverse)
        is PipelineSchedule.CarName -> CarName(name = option.name)
        else -> throw IllegalArgumentException("Unknown option: $option")
      }
    }?.plus(
      listOf(
        CarAvailability(availability = true),
        StationNames(names = PREFERRED_STATIONS.split(","))
      )
    )
    ?: throw IllegalArgumentException("No pipeline schedule found")
