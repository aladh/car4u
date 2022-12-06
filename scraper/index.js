import { checkAvailability } from './src/checker.js'

const [startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute] = process.argv.slice(2)[0].split(' ')
const dateTimeComponents = { startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute }

const result = {
  bookingStart: formatDateTime(startYear, startMonth, startDay, startHour, startMinute),
  bookingEnd: formatDateTime(endYear, endMonth, endDay, endHour, endMinute),
  stations: await checkAvailability({ username: process.env.USERNAME, password: process.env.PASSWORD, dateTimeComponents })
}

process.stdout.write(JSON.stringify(result, null, 2))

function formatDateTime(year, month, day, hour, minute) {
  return `${year}-${month}-${day} ${hour}:${minute}`
}
