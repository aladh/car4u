import { checkAvailability } from './src/checker.js'
import fs from 'fs'

const [startMonth, startDay, startHour, startMinute, endMonth, endDay, endHour, endMinute] = process.argv.slice(2)[0].split(' ')
const dateTimeComponents = { startMonth, startDay, startHour, startMinute, endMonth, endDay, endHour, endMinute }
const status = {
  bookingStart: formatDateTime(startMonth, startDay, startHour, startMinute),
  bookingEnd: formatDateTime(endMonth, endDay, endHour, endMinute)
}

console.log(`Running for booking times: ${status.bookingStart} to ${status.bookingEnd}`)

status.stations = await checkAvailability({ username: process.env.USERNAME, password: process.env.PASSWORD, dateTimeComponents })

fs.writeFileSync('status.json', JSON.stringify(status, null, 2))

function formatDateTime(month, day, hour, minute) {
  return `${month}-${day} ${hour}:${minute}`
}