package fetch.car.pages

import AvailabilityReport
import com.microsoft.playwright.Page

class ReservationGridPage(private val page: Page) {
  companion object {
    const val STATION_DIV_SELECTOR = ".tblAv"
    const val STATION_NAME_SELECTOR = ".strongName a"
    const val CAR_DIV_SELECTOR = ".bRow"
    const val CAR_NAME_SELECTOR = ".divDesc"
    const val CAR_AVAILABILITY_SELECTOR = ".cBCar strong"
    private const val CAR_AVAILABLE_STRING = "Select"

    fun exists(page: Page): Boolean = page.textContent("body").contains("   Reservation Grid")
  }

  fun availability(): AvailabilityReport {
    val stations: MutableList<AvailabilityReport.Station> = mutableListOf()

    page.querySelectorAll(STATION_DIV_SELECTOR).forEach { stationDiv ->
      val stationName = stationDiv.querySelector(STATION_NAME_SELECTOR)?.textContent()?.trim()
      checkNotNull(stationName) { "Station name not found" }

      val cars: MutableList<AvailabilityReport.Car> = mutableListOf()

      stationDiv.querySelectorAll(CAR_DIV_SELECTOR).forEach { carDiv ->
        val carName = carDiv.querySelector(CAR_NAME_SELECTOR)?.textContent()?.trim()
        checkNotNull(carName) { "Car name not found" }

        val carAvailability = carDiv.querySelector(CAR_AVAILABILITY_SELECTOR)?.textContent()?.trim()
        checkNotNull(carAvailability) { "Car availability not found" }

        cars.add(
          AvailabilityReport.Car(
            name = carName,
            available = carAvailability == CAR_AVAILABLE_STRING
          )
        )
      }

      stations.add(AvailabilityReport.Station(stationName, cars))
    }

    return AvailabilityReport(stations)
  }
}
