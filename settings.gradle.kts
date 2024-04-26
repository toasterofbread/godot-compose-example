rootProject.name = "test"

include(":plugin")
include(":app")

pluginManagement {
    repositories {
        mavenLocal()
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    plugins {
        val kotlin_version: String = extra["kotlin.version"] as String
        val agp_version: String = extra["agp.version"] as String
        val compose_version: String = extra["compose.version"] as String

        kotlin("android").version(kotlin_version)
        id("com.android.library").version(agp_version)
        id("org.jetbrains.compose").version(compose_version)
    }
}
