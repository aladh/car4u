package notification

import io.ktor.client.request.*
import kotlinx.serialization.Serializable

@Serializable
private data class Payload(val content: String)

suspend fun sendWebhook(url: String, message: String) {
  client.post(url) {
    setBody(Payload(message))
  }
}
