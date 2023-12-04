package filter

import AvailabilityReport

fun interface StationFilter {
  fun filter(stations: List<AvailabilityReport.Station>): List<AvailabilityReport.Station>
}
