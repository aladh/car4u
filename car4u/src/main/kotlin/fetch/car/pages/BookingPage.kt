package fetch.car.pages

import ReservationTime
import com.microsoft.playwright.Page

class BookingPage(private val page: Page) {
  enum class Selector(val value: String) {
    START_YEAR("input[name=StartYear]"),
    START_MONTH("input[name=StartMonth]"),
    START_DAY("input[name=StartDay]"),
    START_HOUR("select[name=StartHour]"),
    START_MINUTE("select[name=StartMinute]"),
    END_YEAR("input[name=EndYear]"),
    END_MONTH("input[name=EndMonth]"),
    END_DAY("input[name=EndDay]"),
    END_HOUR("select[name=EndHour]"),
    END_MINUTE("select[name=EndMinute]"),
    SEARCH_BUTTON("#Button_Disponibility"),
    STATION_DROPDOWN("#ddcl-StationID"),
    ALL_STATIONS_CHECKBOX("#ddcl-StationID-i1")
  }

  companion object {
    // Navigate to the iframe that contains the reservation grid
    const val URL = "https://www.reservauto.net/Scripts/Client/ReservationAdd.asp?ReactIframe=true&CurrentLanguageID=2"
  }

  init {
    page.navigate(URL)
  }

  fun search(reservationTime: ReservationTime) {
    fillDateTimeComponent(Selector.START_YEAR, reservationTime.startYear)
    fillDateTimeComponent(Selector.START_MONTH, reservationTime.startMonth)
    fillDateTimeComponent(Selector.START_DAY, reservationTime.startDay)
    fillDateTimeComponent(Selector.START_HOUR, reservationTime.startHour)
    fillDateTimeComponent(Selector.START_MINUTE, reservationTime.startMinute)
    fillDateTimeComponent(Selector.END_YEAR, reservationTime.endYear)
    fillDateTimeComponent(Selector.END_MONTH, reservationTime.endMonth)
    fillDateTimeComponent(Selector.END_DAY, reservationTime.endDay)
    fillDateTimeComponent(Selector.END_HOUR, reservationTime.endHour)
    fillDateTimeComponent(Selector.END_MINUTE, reservationTime.endMinute)

    // Select option to show all stations on reservation grid
    page.click(Selector.STATION_DROPDOWN.value)
    page.click(Selector.ALL_STATIONS_CHECKBOX.value)

    page.click(Selector.SEARCH_BUTTON.value)
  }

  private fun fillDateTimeComponent(selector: Selector, value: String) {
    page.evalOnSelector(selector.value, "el => el.value = '$value'")
  }
}
