data class ReservationTime(
  val startYear: String,
  val startMonth: String,
  val startDay: String,
  val startHour: String,
  val startMinute: String,
  val endYear: String,
  val endMonth: String,
  val endDay: String,
  val endHour: String,
  val endMinute: String
) {
  val bookingStart: String
    get() = "$startYear-$startMonth-$startDay $startHour:$startMinute"

  val bookingEnd: String
    get() = "$endYear-$endMonth-$endDay $endHour:$endMinute"
}
