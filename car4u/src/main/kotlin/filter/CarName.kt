package filter

import ReservationGrid

class CarName(private val name: String) : StationFilter {
  override fun filter(stations: List<ReservationGrid.Station>): List<ReservationGrid.Station> =
    stations.mapNotNull {
      val matchingCars = it.carsWithName(name)

      if (matchingCars.isEmpty()) return@mapNotNull null

      it.copy(cars = matchingCars)
    }

  override fun toString(): String = "CarName(name=$name)"
}

private fun ReservationGrid.Station.carsWithName(name: String): List<ReservationGrid.Station.Car> =
  cars.filter { it.hasName(name) }

private fun ReservationGrid.Station.Car.hasName(name: String): Boolean =
  this.name.lowercase().contains(name.lowercase())
