const STATION_DIV_SELECTOR = '.tblAv'
const STATION_NAME_SELECTOR = '.stationName'
const CAR_DIV_SELECTOR = '.bRow'
const CAR_NAME_SELECTOR = '.divDesc'
const CAR_AVAILABILITY_SELECTOR = '.cBCar strong'

export default class ReservationGrid {
  constructor(page) {
    this.page = page
  }

  async stations() {
    const stations = []
    const stationDivs = await this.page.$$(STATION_DIV_SELECTOR)

    for (const stationDiv of stationDivs) {
      const station = {
        name: await stationDiv.$eval(STATION_NAME_SELECTOR, e => e.textContent),
        cars: []
      }

      const carDivs = await stationDiv.$$(CAR_DIV_SELECTOR)

      for (const carDiv of carDivs) {
        station.cars.push({
          name: await carDiv.$eval(CAR_NAME_SELECTOR, e => e.innerText),
          available: await carDiv.$eval(CAR_AVAILABILITY_SELECTOR, e => e.innerText) === 'Select'
        })
      }

      stations.push(station)
    }

    return stations
  }
}
