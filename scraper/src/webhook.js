export async function send(url, message) {
  let res = await fetch(url, {
    method: 'POST',
    body: JSON.stringify({ content: message }),
    headers: { 'Content-Type': 'application/json' }
  })

  if (res.status !== 204) {
    console.error(`Received bad status code from webhook request: ${res.status}`)
  }
}
