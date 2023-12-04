package filter

import AvailabilityReport

class CarAvailability(private val availability: Boolean) : StationFilter {
  override fun filter(stations: List<AvailabilityReport.Station>): List<AvailabilityReport.Station> =
    stations.mapNotNull {
      val matchingCars = it.carsWithAvailability(availability)

      if (matchingCars.isEmpty()) return@mapNotNull null

      it.copy(cars = matchingCars)
    }

  override fun toString(): String = "CarAvailability(availability=$availability)"
}

private fun AvailabilityReport.Station.carsWithAvailability(availability: Boolean): List<AvailabilityReport.Car> =
  cars.filter { it.available == availability }
