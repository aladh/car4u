export default class ReservationGrid {
  constructor(page) {
    this.page = page
  }

  async hasStationsAvailable() {
    const selectButtons = await this.page.$x("//a[contains(., 'Select')]")

    return selectButtons.length > 0
  }

  async stationsJSON() {
    const stations = []
    const stationDivs = await this.page.$$('.tblAv')

    for (const stationDiv of stationDivs) {
      const station = {}

      const stationNameA = await stationDiv.$('.strongName a')
      station.name = await this.page.evaluate(e => e.textContent, stationNameA)

      const cars = []
      const carDivs = await stationDiv.$$('.bRow')

      for (const carDiv of carDivs) {
        const car = {}

        const carNameDiv = await carDiv.$('.divDesc')
        car.name = await this.page.evaluate(e => e.innerText, carNameDiv)

        const carAvailableContainer = await carDiv.$('.cBCar strong')
        const carAvailableContainerText = await this.page.evaluate(e => e.innerText, carAvailableContainer)
        car.available = carAvailableContainerText === 'Select'

        cars.push(car)
      }

      station.cars = cars

      stations.push(station)
    }

    return stations
  }

  async availableStationNames() {
    const availableStationNames = []

    const selectButtons = await this.page.$x("//a[contains(., 'Select')]")

    for (const selectButton of selectButtons) {
      const [station] = await selectButton.$$("xpath=./ancestor::div[contains(concat(' ', @class, ' '), ' tblAv ')]//div[contains(concat(' ', @class, ' '), ' strongName ')]//a")
      const stationName = await this.page.evaluate(e => e.textContent, station)
      availableStationNames.push(stationName)
    }

    return availableStationNames
  }
}
