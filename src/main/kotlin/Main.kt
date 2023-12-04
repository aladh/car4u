import fetch.car.fetchAvailabilityReport
import fetch.gitlab.fetchPipelineSchedule
import filter.CarAvailability
import filter.CarName
import filter.StationFilter
import filter.StationNames
import notification.sendWebhook

private val WEBHOOK_URL: String = System.getenv("WEBHOOK_URL").also {
  requireNotNull(it) { "WEBHOOK_URL environment variable not set" }
}

private val PREFERRED_STATIONS: String = System.getenv("PREFERRED_STATIONS").also {
  requireNotNull(it) { "PREFERRED_STATIONS environment variable not set" }
}

private val USERNAME: String = System.getenv("USERNAME").also {
  requireNotNull(it) { "USERNAME environment variable not set" }
}

private val PASSWORD: String = System.getenv("PASSWORD").also {
  requireNotNull(it) { "PASSWORD environment variable not set" }
}

private val READ_SCHEDULES_TOKEN: String? = System.getenv("READ_SCHEDULES_TOKEN")
private val CI_PROJECT_ID: String? = System.getenv("CI_PROJECT_ID")
private val CI_PIPELINE_ID: String? = System.getenv("CI_PIPELINE_ID")

// Used for running outside of GitLab CI
private val PIPELINE_SCHEDULE_DESCRIPTION: String? = System.getenv("PIPELINE_SCHEDULE_DESCRIPTION")

suspend fun main() {
  val schedule =
    PIPELINE_SCHEDULE_DESCRIPTION
      ?.let { PipelineSchedule(it) }
      ?: fetchPipelineSchedule(
        readSchedulesToken = READ_SCHEDULES_TOKEN,
        ciProjectId = CI_PROJECT_ID,
        ciPipelineId = CI_PIPELINE_ID
      )
  checkNotNull(schedule) { "No pipeline schedule found" }

  val reservationTime = schedule.reservationTime().also {
    println("Reservation: ${it.bookingStart} - ${it.bookingEnd}")
  }

  val availabilityReport = fetchAvailabilityReport(
    username = USERNAME,
    password = PASSWORD,
    reservationTime = reservationTime
  )

  availabilityReport
    .filteredStations(schedule)
    .forEach { station ->
      sendWebhook(
        WEBHOOK_URL,
        "Date: ${reservationTime.bookingStart} - ${reservationTime.bookingEnd}\nStation: ${station.name}\n" +
          "Cars: ${station.cars.joinToString { it.name }}"
      )
    }
}

private fun AvailabilityReport.filteredStations(schedule: PipelineSchedule): List<AvailabilityReport.Station> =
  schedule
    .filters()
    .also { println("Filters: $it") }
    .fold(stations) { stations, filter ->
      filter.filter(stations)
    }

private fun PipelineSchedule.filters(): List<StationFilter> =
  options()
    .mapToFilters()
    .plus(
      listOf(
        CarAvailability(availability = true),
        StationNames(names = PREFERRED_STATIONS.split(","))
      )
    )

private fun List<PipelineSchedule.Option>.mapToFilters(): List<StationFilter> =
  map {
    when (it) {
      is PipelineSchedule.StationNames -> StationNames(names = it.names, inverse = it.inverse)
      is PipelineSchedule.CarName -> CarName(name = it.name)
      else -> throw IllegalArgumentException("Unknown option: $it")
    }
  }
