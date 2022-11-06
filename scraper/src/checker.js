import puppeteer from 'puppeteer'
import LoginPage from './page_objects/login_page.js'
import BookingPage from './page_objects/booking_page.js'

export async function checkAvailability({
                                          username,
                                          password,
                                          startMonth,
                                          startDay,
                                          startHour,
                                          startMinute,
                                          endMonth,
                                          endDay,
                                          endHour,
                                          endMinute
                                        }) {
  const browser = await puppeteer.launch({
    args: ['--no-sandbox'],
    headless: true,
    defaultViewport: {
      height: 1920,
      width: 1080
    }
  })
  const page = await browser.newPage()

  try {
    const loginPage = new LoginPage(page)

    await loginPage.goto()
    await loginPage.login({ username, password })

    await page.waitForTimeout(5000)

    const bookingPage = new BookingPage(page)

    await bookingPage.goto()
    const reservationGrid = await bookingPage.search({
      startMonth,
      startDay,
      startHour,
      startMinute,
      endMonth,
      endDay,
      endHour,
      endMinute
    })

    await page.screenshot({ path: 'reservation_grid.png', fullPage: true })

    let stations = await reservationGrid.stations()

    await browser.close()

    return stations
  } catch (e) {
    await page.screenshot({ path: 'error.png' })
    throw(e)
  }
}
