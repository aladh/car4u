package filter

import ReservationGrid

class StationNames(private val names: List<String>, private val inverse: Boolean = false) : StationFilter {
  override fun filter(stations: List<ReservationGrid.Station>): List<ReservationGrid.Station> =
    stations.filter {
      if (inverse) !it.hasAnyName(names) else it.hasAnyName(names)
    }

  override fun toString(): String = "StationNames(names=$names, inverse=$inverse)"
}

private fun ReservationGrid.Station.hasAnyName(names: List<String>) =
  name.lowercase().containsAny(names.map { it.lowercase() })

private fun String.containsAny(names: List<String>): Boolean =
  names.any { this.contains(it) }
