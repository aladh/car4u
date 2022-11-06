import { checkAvailability } from './src/checker.js'
import fs from 'fs'

const [startMonth, startDay, startHour, startMinute, endMonth, endDay, endHour, endMinute] = process.argv.slice(2)[0].split(' ')

console.log(`Running for booking times: ${[startMonth, startDay, startHour, startMinute, endMonth, endDay, endHour, endMinute]}`)

const stations = await checkAvailability({
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

const status = {
  bookingStart: `${startMonth}-${startDay} ${startHour}:${startMinute}`,
  bookingEnd: `${endMonth}-${endDay} ${endHour}:${endMinute}`,
  stations: stations
}

fs.writeFileSync('status.json', JSON.stringify(status, null, 2))
