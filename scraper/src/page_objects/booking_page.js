import ReservationGrid from "./reservation_grid.js";

export default class BookingPage {
  constructor(page) {
    this.page = page
  }

  async goto() {
    await this.page.goto('https://www.reservauto.net/Scripts/Client/ReservationAdd.asp?ReactIframe=true&CurrentLanguageID=2')
    await this.page.waitForTimeout(1000)
  }

  async search({
                 startMonth,
                 startDay,
                 startHour,
                 startMinute,
                 endMonth,
                 endDay,
                 endHour,
                 endMinute
               }) {
    await this.page.$eval('input[name=StartMonth]', (el, value) => el.value = value, startMonth)
    await this.page.$eval('input[name=StartDay]', (el, value) => el.value = value, startDay)
    await this.page.select('select[name=StartHour]', startHour)
    await this.page.select('select[name=StartMinute]', startMinute)

    await this.page.$eval('input[name=EndMonth]', (el, value) => el.value = value, endMonth)
    await this.page.$eval('input[name=EndDay]', (el, value) => el.value = value, endDay)
    await this.page.select('select[name=EndHour]', endHour)
    await this.page.select('select[name=EndMinute]', endMinute)

    await this.page.click('#Button_Disponibility')
    await this.page.waitForTimeout(2000)

    return new ReservationGrid(this.page)
  }
}
