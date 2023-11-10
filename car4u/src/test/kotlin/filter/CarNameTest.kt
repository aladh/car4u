package filter

import ReservationGrid
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

class CarNameTest : DescribeSpec({
  val station1 = ReservationGrid.Station("1", listOf(ReservationGrid.Station.Car("Horse", true)))
  val station2 = ReservationGrid.Station("2", listOf(ReservationGrid.Station.Car("Pony", true)))
  val station3 = ReservationGrid.Station(
    "3",
    listOf(ReservationGrid.Station.Car("Husky", true), ReservationGrid.Station.Car("Horse", true))
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
