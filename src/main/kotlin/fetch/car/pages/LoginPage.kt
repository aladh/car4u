package fetch.car.pages

import com.microsoft.playwright.Page

class LoginPage(private val page: Page) {
  companion object {
    const val URL = "https://ontario.client.reservauto.net/bookCar"
    const val USERNAME_SELECTOR = "input[name=Username]"
    const val PASSWORD_SELECTOR = "input[name=Password]"
    const val SUBMIT_SELECTOR = "button[data-testid=submit]"
  }

  init {
    page.navigate(URL)
  }

  fun login(username: String, password: String) {
    page.fill(USERNAME_SELECTOR, username)
    page.fill(PASSWORD_SELECTOR, password)
    page.click(SUBMIT_SELECTOR)
  }
}
