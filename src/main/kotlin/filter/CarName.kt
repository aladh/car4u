package filter

import AvailabilityReport

class CarName(private val name: String) : StationFilter {
  override fun filter(stations: List<AvailabilityReport.Station>): List<AvailabilityReport.Station> =
    stations.mapNotNull {
      val matchingCars = it.carsWithName(name)

      if (matchingCars.isEmpty()) return@mapNotNull null

      it.copy(cars = matchingCars)
    }

  override fun toString(): String = "CarName(name=$name)"
}

private fun AvailabilityReport.Station.carsWithName(name: String): List<AvailabilityReport.Car> =
  cars.filter { it.hasName(name) }

private fun AvailabilityReport.Car.hasName(name: String): Boolean =
  this.name.lowercase().contains(name.lowercase())
