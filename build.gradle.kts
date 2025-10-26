import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


plugins {
    kotlin("jvm") version "1.9.10"
    id("org.jetbrains.intellij") version "1.17.4"
    id("com.diffplug.spotless") version "6.25.0"
}



group = "xxrexraptorxx"
version = "0.1.0"


repositories {
    mavenCentral()
}


dependencies {
    implementation("org.commonmark:commonmark:0.21.0")
}


intellij {
    version.set("2025.2")
    plugins.set(listOf())
}


tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}
