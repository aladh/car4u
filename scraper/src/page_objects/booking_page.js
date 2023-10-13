import ReservationGrid from './reservation_grid.js'

const SELECTORS = {
  startYear: 'input[name=StartYear]',
  startMonth: 'input[name=StartMonth]',
  startDay: 'input[name=StartDay]',
  startHour: 'select[name=StartHour]',
  startMinute: 'select[name=StartMinute]',
  endYear: 'input[name=EndYear]',
  endMonth: 'input[name=EndMonth]',
  endDay: 'input[name=EndDay]',
  endHour: 'select[name=EndHour]',
  endMinute: 'select[name=EndMinute]',
  searchButton: '#Button_Disponibility',
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
    await this.page.waitForSelector(SELECTORS.startMonth)
  }

  async search({ startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute }) {
    await this.page.$eval(SELECTORS.startYear, (el, value) => el.value = value, startYear)
    await this.page.$eval(SELECTORS.startMonth, (el, value) => el.value = value, startMonth)
    await this.page.$eval(SELECTORS.startDay, (el, value) => el.value = value, startDay)
    await this.page.select(SELECTORS.startHour, startHour)
    await this.page.select(SELECTORS.startMinute, startMinute)

    await this.page.$eval(SELECTORS.endYear, (el, value) => el.value = value, endYear)
    await this.page.$eval(SELECTORS.endMonth, (el, value) => el.value = value, endMonth)
    await this.page.$eval(SELECTORS.endDay, (el, value) => el.value = value, endDay)
    await this.page.select(SELECTORS.endHour, endHour)
    await this.page.select(SELECTORS.endMinute, endMinute)

    // Select option to show all stations on reservation grid
    await this.page.click(SELECTORS.stationDropdown)
    await this.page.click(SELECTORS.allStationsCheckbox)

    await this.page.click(SELECTORS.searchButton)
    await new Promise(r => setTimeout(r, 2000))

    return new ReservationGrid(this.page)
  }
}
