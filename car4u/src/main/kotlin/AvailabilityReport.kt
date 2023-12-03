data class AvailabilityReport(val stations: List<Station>) {
  data class Station(val name: String, val cars: List<Car>) {
    constructor(stationName: String, carName: String) : this(
      stationName,
      listOf(Car(carName, true))
    )
  }

  data class Car(val name: String, val available: Boolean)
}
