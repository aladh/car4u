import ReservationGrid from './reservation_grid.js'

const START_YEAR_SELECTOR = 'input[name=StartYear]'
const START_MONTH_SELECTOR = 'input[name=StartMonth]'
const START_DAY_SELECTOR = 'input[name=StartDay]'
const START_HOUR_SELECTOR = 'select[name=StartHour]'
const START_MINUTE_SELECTOR = 'select[name=StartMinute]'
const END_YEAR_SELECTOR = 'input[name=EndYear]'
const END_MONTH_SELECTOR = 'input[name=EndMonth]'
const END_DAY_SELECTOR = 'input[name=EndDay]'
const END_HOUR_SELECTOR = 'select[name=EndHour]'
const END_MINUTE_SELECTOR = 'select[name=EndMinute]'
const SEARCH_BUTTON_SELECTOR = '#Button_Disponibility'

const SELECTORS = {
  stationDropdown: '#ddcl-StationID',
  allStationsCheckbox: '#ddcl-StationID-i1',
}

export default class BookingPage {
  constructor(page) {
    this.page = page
  }

  // Navigate to the iframe that contains the reservation grid
  async goto() {
    await this.page.goto('https://www.reservauto.net/Scripts/Client/ReservationAdd.asp?ReactIframe=true&CurrentLanguageID=2')
    await this.page.waitForSelector(START_MONTH_SELECTOR)
  }

  async search({ startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute }) {
    await this.page.$eval(START_YEAR_SELECTOR, (el, value) => el.value = value, startYear)
    await this.page.$eval(START_MONTH_SELECTOR, (el, value) => el.value = value, startMonth)
    await this.page.$eval(START_DAY_SELECTOR, (el, value) => el.value = value, startDay)
    await this.page.select(START_HOUR_SELECTOR, startHour)
    await this.page.select(START_MINUTE_SELECTOR, startMinute)

    await this.page.$eval(END_YEAR_SELECTOR, (el, value) => el.value = value, endYear)
    await this.page.$eval(END_MONTH_SELECTOR, (el, value) => el.value = value, endMonth)
    await this.page.$eval(END_DAY_SELECTOR, (el, value) => el.value = value, endDay)
    await this.page.select(END_HOUR_SELECTOR, endHour)
    await this.page.select(END_MINUTE_SELECTOR, endMinute)

    // Select option to show all stations on reservation grid
    await this.page.click(SELECTORS.stationDropdown)
    await this.page.click(SELECTORS.allStationsCheckbox)

    await this.page.click(SEARCH_BUTTON_SELECTOR)
    await new Promise(r => setTimeout(r, 2000))

    return new ReservationGrid(this.page)
  }
}
