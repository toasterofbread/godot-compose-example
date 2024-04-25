rootProject.name = "test"

include(":plugin")
include(":app")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    plugins {
        val kotlin_version: String = extra["kotlin.version"] as String
        val godot_kotlin_version: String = extra["godotkotlin.version"] as String
        val agp_version: String = extra["agp.version"] as String

        kotlin("android").version(kotlin_version)
        id("com.utopia-rise.godot-kotlin-jvm").version(godot_kotlin_version)
        id("com.android.library").version(agp_version)
    }
}
