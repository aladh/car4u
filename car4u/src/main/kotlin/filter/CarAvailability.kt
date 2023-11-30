package filter

import ReservationGrid

class CarAvailability(private val availability: Boolean) : StationFilter {
  override fun filter(stations: List<ReservationGrid.Station>): List<ReservationGrid.Station> =
    stations.mapNotNull {
      val matchingCars = it.carsWithAvailability(availability)

      if (matchingCars.isEmpty()) return@mapNotNull null

      it.copy(cars = matchingCars)
    }

  override fun toString(): String = "CarAvailability(availability=$availability)"
}

private fun ReservationGrid.Station.carsWithAvailability(availability: Boolean): List<ReservationGrid.Station.Car> =
  cars.filter { it.available == availability }
