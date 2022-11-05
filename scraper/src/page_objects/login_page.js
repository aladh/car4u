export default class LoginPage {
  constructor(page) {
    this.page = page
  }

  async goto() {
    const LOGIN_URL = 'https://ontario.client.reservauto.net/bookCar'

    await this.page.goto(LOGIN_URL)
    await this.page.waitForSelector('input[name=Username]', { timeout: 5000 })
  }

  async login({ username, password }) {
    const USERNAME_SELECTOR = 'input[name=Username]'
    const PASSWORD_SELECTOR = 'input[name=Password]'
    const SUBMIT_SELECTOR = 'button[data-testid=submit]'

    await this.page.type(USERNAME_SELECTOR, username)
    await this.page.type(PASSWORD_SELECTOR, password)

    await this.page.waitForTimeout(1000)

    await this.page.click(SUBMIT_SELECTOR)
  }
}
