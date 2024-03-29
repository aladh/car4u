package filter

import AvailabilityReport
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder

class StationNamesTest : DescribeSpec({
  val station1 = AvailabilityReport.Station("01 - Station 1", listOf(AvailabilityReport.Car("car", true)))
  val station2 = AvailabilityReport.Station("02 - Station 2", listOf(AvailabilityReport.Car("car", true)))
  val station3 = AvailabilityReport.Station("03 - No match", listOf(AvailabilityReport.Car("car", true)))
  val stations = listOf(station1, station2, station3)

  describe("filter") {
    context("with one name") {
      it("returns stations that contain the name") {
        StationNames(listOf("Station")).filter(stations) shouldContainExactlyInAnyOrder (listOf(station1, station2))
      }
    }

    context("with multiple names") {
      it("returns stations that contain any of the names") {
        StationNames(listOf("Station", "03")).filter(stations) shouldContainExactlyInAnyOrder listOf(
          station1,
          station2,
          station3
        )
      }
    }

    context("when station matches with a different case") {
      it("returns stations that contain the name") {
        StationNames(listOf("station")).filter(stations) shouldContainExactlyInAnyOrder (listOf(station1, station2))
      }
    }

    context("when inverse is true") {
      it("returns stations that don't contain the name") {
        StationNames(listOf("01"), inverse = true).filter(stations) shouldContainExactlyInAnyOrder listOf(
          station2,
          station3
        )
      }
    }
  }
})
