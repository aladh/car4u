const LOGIN_URL = 'https://ontario.client.reservauto.net/bookCar'

const USERNAME_SELECTOR = 'input[name=Username]'
const PASSWORD_SELECTOR = 'input[name=Password]'
const SUBMIT_SELECTOR = 'button[data-testid=submit]'

export default class LoginPage {
  constructor(page) {
   this.page = page
  }

  async goto() {
    await this.page.goto(LOGIN_URL)
    await this.page.waitForSelector(USERNAME_SELECTOR)
  }

  async login({ username, password }) {
    await this.page.type(USERNAME_SELECTOR, username)
    await this.page.type(PASSWORD_SELECTOR, password)

    // Wait for typing to complete
    await new Promise(r => setTimeout(r, 1000))

    await this.page.click(SUBMIT_SELECTOR)
    await this.page.waitForNavigation()
  }
}
