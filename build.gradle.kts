import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
  id("com.github.johnrengelman.shadow") version "5.2.0"
  java
  kotlin("jvm") version "1.4.32"
}

group = "com.jakubtomana.minecraft.serverstresstool"
version = "1.0.2"

repositories {
  mavenLocal()
  mavenCentral()
  maven(url = "https://jitpack.io")
}

dependencies {
  testImplementation("junit:junit:4.12")
  implementation("com.github.Eoghanmc22:McPacketLib:e6af0f7876")
  implementation("com.github.Eoghanmc22:packets:6383f00cff")
  implementation("commons-cli:commons-cli:1.4")
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.3")
}

tasks.named<ShadowJar>("shadowJar") {
  manifest {
    attributes["Main-Class"] = "space.buzzles.serverstresstool.Loader"
    attributes["Multi-Release"] = true
  }
}
