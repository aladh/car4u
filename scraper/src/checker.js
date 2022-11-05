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

  let availableStations
  let stationsJSON

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

    await reservationGrid.hasStationsAvailable() ? console.log('Found available station(s)') : console.log('No stations are available')

    availableStations = await reservationGrid.availableStationNames()
    stationsJSON = await reservationGrid.stationsJSON()

  } catch (e) {
    await page.screenshot({ path: 'error.png' })

    console.log(`Caught error: ${e}`)
    console.log(e.stack)

    process.exitCode = 1
  }

  await browser.close()

  return { availableStations, stationsJSON }
}
