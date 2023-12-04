package filter

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

class CarNameTest : DescribeSpec({
  val station1 = AvailabilityReport.Station("1", listOf(AvailabilityReport.Car("Horse", true)))
  val station2 = AvailabilityReport.Station("2", listOf(AvailabilityReport.Car("Pony", true)))
  val station3 = AvailabilityReport.Station(
    "3",
    listOf(AvailabilityReport.Car("Husky", true), AvailabilityReport.Car("Horse", true))
  )
  val stations = listOf(station1, station2, station3)

  describe("filter") {
    it("returns stations that have a car with the name") {
      CarName("Horse").filter(stations)
        .map { it.name } shouldContainExactlyInAnyOrder (listOf(station1.name, station3.name))
    }

    it("returns only cars containing the name") {
      CarName("Horse").filter(stations).let {
        it.forEach { station ->
          station.cars.forEach { car ->
            car.name shouldBe "Horse"
          }
        }
      }
    }

    context("when car matches with a different case") {
      it("returns stations that have a car with the name") {
        CarName("pony").filter(stations).map { it.name } shouldContainExactlyInAnyOrder (listOf(station2.name))
      }
    }
  }
})
