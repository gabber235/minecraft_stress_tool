import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("com.github.johnrengelman.shadow") version "5.2.0"
  java
  kotlin("jvm") version "1.5.31"
}

group = "com.jakubtomana.minecraft.serverstresstool"
version = "1.0.2"

repositories {
  mavenLocal()
  mavenCentral()
  maven(url = "https://jitpack.io")
}

dependencies {
  testImplementation("junit:junit:4.13.2")
  implementation("com.github.GeyserMC:PacketLib:2.0")
  implementation("com.github.Steveice10:MCProtocolLib:1.17.1-1")
  implementation("commons-cli:commons-cli:1.4")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
}

tasks.named<ShadowJar>("shadowJar") {
  manifest {
    attributes["Main-Class"] = "space.buzzles.serverstresstool.Loader"
    attributes["Multi-Release"] = true
  }
}
