import kotlinx.serialization.Serializable

@Serializable
data class ReservationGrid(val bookingStart: String, val bookingEnd: String, val stations: List<Station>) {
  @Serializable
  data class Station(val name: String, val cars: List<Car>) {
    @Serializable
    data class Car(val name: String, val available: Boolean)
  }
}
