package filter

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

class CarAvailabilityTest : DescribeSpec({
  val station1 = AvailabilityReport.Station("1", listOf(AvailabilityReport.Car("Horse", true)))
  val station2 = AvailabilityReport.Station("2", listOf(AvailabilityReport.Car("Pony", false)))
  val station3 = AvailabilityReport.Station(
    "3",
    listOf(AvailabilityReport.Car("Husky", true), AvailabilityReport.Car("Horse", false))
  )
  val stations = listOf(station1, station2, station3)

  describe("filter") {
    context("when desired availability is true") {
      it("returns stations that have an available car") {
        CarAvailability(true).filter(stations)
          .map { it.name } shouldContainExactlyInAnyOrder (listOf(station1.name, station3.name))
      }

      it("returns only available cars") {
        CarAvailability(true).filter(stations).let {
          it.forEach { station ->
            station.cars.forEach { car ->
              car.available shouldBe true
            }
          }
        }
      }
    }

    context("when desired availability is false") {
      it("returns stations that don't have an available car") {
        CarAvailability(false).filter(stations)
          .map { it.name } shouldContainExactlyInAnyOrder (listOf(station2.name, station3.name))
      }

      it("returns only unavailable cars") {
        CarAvailability(false).filter(stations).let {
          it.forEach { station ->
            station.cars.forEach { car ->
              car.available shouldBe false
            }
          }
        }
      }
    }
  }
})
