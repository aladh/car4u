package notification

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private val serializer = Json { ignoreUnknownKeys = true }

private const val REQUEST_TIMEOUT = 5000L

val client = HttpClient(CIO) {
  expectSuccess = true

  install(DefaultRequest) { contentType(ContentType.Application.Json) }
  install(ContentNegotiation) { json(serializer) }
  install(HttpTimeout) { requestTimeoutMillis = REQUEST_TIMEOUT }
  install(HttpRequestRetry)
}
