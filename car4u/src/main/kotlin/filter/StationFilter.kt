package filter

import ReservationGrid

fun interface StationFilter {
  fun filter(stations: List<ReservationGrid.Station>): List<ReservationGrid.Station>
}
