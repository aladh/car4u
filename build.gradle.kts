plugins {
  kotlin("jvm") version "1.9.21"
  kotlin("plugin.serialization") version "1.9.21"
  application
  id("io.gitlab.arturbosch.detekt") version "1.23.4"
}

group = "ca.alad"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  // detekt linter
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.4")

  // Kotest testing library
  val kotestVersion = "5.7.2"
  testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
  testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
  testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")

  // HTTP client
  val ktorVersion = "2.3.6"
  implementation("io.ktor:ktor-client-core:$ktorVersion")
  implementation("io.ktor:ktor-client-cio:$ktorVersion")
  implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
  implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")

  // JSON serialization
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

  // GitLab API client
  implementation("org.gitlab4j:gitlab4j-api:5.4.0")

  // Browser automation
  implementation("com.microsoft.playwright:playwright:1.28.1")
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(17)
}

application {
  mainClass.set("MainKt")
}

detekt {
  buildUponDefaultConfig = true // preconfigure defaults
  allRules = true // activate all available (even unstable) rules.
  config.setFrom("detekt.yml")
}
