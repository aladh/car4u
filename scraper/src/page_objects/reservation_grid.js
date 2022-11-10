export default class ReservationGrid {
  constructor(page) {
    this.page = page
  }

  async stations() {
    const stations = []
    const stationDivs = await this.page.$$('.tblAv')

    for (const stationDiv of stationDivs) {
      const station = {
        name: await stationDiv.$eval('.strongName a', e => e.textContent),
        cars: []
      }

      const carDivs = await stationDiv.$$('.bRow')

      for (const carDiv of carDivs) {
        station.cars.push({
          name: await carDiv.$eval('.divDesc', e => e.innerText),
          available: await carDiv.$eval('.cBCar strong', e => e.innerText) === 'Select'
        })
      }

      stations.push(station)
    }

    return stations
  }
}
