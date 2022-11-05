import { checkAvailability } from './src/checker.js'
import { send } from './src/webhook.js'
import fs from 'fs'

const PREFERRED_STATIONS = ['069', '035']

const [startMonth, startDay, startHour, startMinute, endMonth, endDay, endHour, endMinute] = process.argv.slice(2)[0].split(' ')

console.log(`Running for booking times: ${[startMonth, startDay, startHour, startMinute, endMonth, endDay, endHour, endMinute]}`)

const { availableStations, stationsJSON } = await checkAvailability({
  username: process.env.USERNAME,
  password: process.env.PASSWORD,
  startMonth,
  startDay,
  startHour,
  startMinute,
  endMonth,
  endDay,
  endHour,
  endMinute
})

for (const station of availableStations) {
  let notify = false

  for (const preferredStation of PREFERRED_STATIONS) {
    if (station.includes(preferredStation)) {
      notify = true
    }
  }

  console.log(station)

  if (notify) {
    await send(process.env.WEBHOOK_URL, `Car(s) available at ${station} on ${[startMonth, startDay, startHour, startMinute, endMonth, endDay, endHour, endMinute]}`)
  }
}

const status = {
  bookingStart: `${startMonth} ${startDay} ${startHour} ${startMinute}`,
  bookingEnd: `${endMonth} ${endDay} ${endHour} ${endMinute}`,
  stations: stationsJSON
}

fs.writeFileSync('status.json', JSON.stringify(status, null, 2))
