const SECONDS_IN_MINUTE = 60
const SECONDS_IN_HOUR = SECONDS_IN_MINUTE * 60

export const formatDuration = (durationSeconds: number) => {
  const hours = Math.floor(durationSeconds / SECONDS_IN_HOUR)
  let remaining = durationSeconds % SECONDS_IN_HOUR
  const minutes = Math.floor(remaining / SECONDS_IN_MINUTE)
  const seconds = remaining % SECONDS_IN_MINUTE

  const hoursString = hours.toLocaleString("en-US", {
    minimumIntegerDigits: 2,
  })

  const minutesString = minutes.toLocaleString("en-US", {
    minimumIntegerDigits: 2,
  })

  const secondsString = seconds.toLocaleString("en-US", {
    minimumIntegerDigits: 2,
  })

  return `${hoursString}:${minutesString}:${secondsString}`
}

export const fromUnixTimestamp = (unixTimestamp: number) => {
  return new Date(unixTimestamp * 1000)
}
