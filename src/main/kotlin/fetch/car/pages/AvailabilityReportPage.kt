package fetch.car.pages

import AvailabilityReport
import com.microsoft.playwright.Page

class AvailabilityReportPage(private val page: Page) {
  companion object {
    private const val SKIP_ROWS = 4
    private const val STATION_NAME_COLUMN = 1
    private const val CAR_NAME_COLUMN = 3
    private const val COLUMN_SELECTOR = "td"
    private const val ROW_SELECTOR = "tr"
    private const val NO_AVAILABILITY_MESSAGE = "No cars are available"

    fun exists(page: Page): Boolean = page.textContent("body").contains("   Availability Report")
  }

  fun availability(): AvailabilityReport {
    if (page.textContent("body").contains(NO_AVAILABILITY_MESSAGE)) return AvailabilityReport(emptyList())

    val stations: MutableList<AvailabilityReport.Station> = mutableListOf()

    page.querySelectorAll(ROW_SELECTOR).drop(SKIP_ROWS).forEach {
      val columns = it.querySelectorAll(COLUMN_SELECTOR)

      stations.add(
        AvailabilityReport.Station(
          stationName = columns[STATION_NAME_COLUMN].textContent().trim(),
          carName = columns[CAR_NAME_COLUMN].textContent().trim()
        )
      )
    }

    return AvailabilityReport(stations)
  }
}
