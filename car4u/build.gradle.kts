plugins {
  kotlin("jvm") version "1.9.20"
  application
  id("io.gitlab.arturbosch.detekt") version "1.23.3"
}

group = "ca.alad"
version = "1.0-SNAPSHOT"

repositories {
  mavenCentral()
}

dependencies {
  // detekt linter
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.3")

  // Kotest testing library
  val kotestVersion = "5.7.2"
  testImplementation("io.kotest:kotest-runner-junit5:$kotestVersion")
  testImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
  testImplementation("io.kotest:kotest-framework-datatest:$kotestVersion")
}

tasks.test {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(8)
}

application {
  mainClass.set("MainKt")
}

detekt {
  buildUponDefaultConfig = true // preconfigure defaults
  allRules = true // activate all available (even unstable) rules.
  config.setFrom("detekt.yml")
}
