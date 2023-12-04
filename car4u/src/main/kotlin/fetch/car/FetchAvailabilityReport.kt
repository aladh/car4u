package fetch.car

import AvailabilityReport
import ReservationTime
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.WaitUntilState
import fetch.car.pages.AvailabilityReportPage
import fetch.car.pages.BookingPage
import fetch.car.pages.LoginPage
import fetch.car.pages.ReservationGridPage

private class PageNotFoundException : RuntimeException()

fun fetchAvailabilityReport(username: String, password: String, reservationTime: ReservationTime): AvailabilityReport =
  withPage { page ->
    LoginPage(page).login(username, password)

    page.waitForNavigation()

    BookingPage(page).search(reservationTime)

    page.waitForLoadState()

    when {
      AvailabilityReportPage.exists(page) -> {
        AvailabilityReportPage(page).availability()
      }

      ReservationGridPage.exists(page) -> {
        ReservationGridPage(page).availability()
      }

      else -> throw PageNotFoundException()
    }
  }

private fun Page.waitForNavigation() {
  waitForNavigation(Page.WaitForNavigationOptions().setWaitUntil(WaitUntilState.NETWORKIDLE)) {}
}

private fun <T> withPage(block: (Page) -> T): T {
  val playwright = Playwright.create()
  val page = playwright.webkit().launch().newPage()

  val result = block(page)

  playwright.close()

  return result
}
