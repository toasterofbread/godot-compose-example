plugins {
    kotlin("android")
    id("com.android.library")
}

kotlin {
    jvmToolchain(17)
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "dev.toastbits.webviewtest.plugin"

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
    }

    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_17)
        targetCompatibility(JavaVersion.VERSION_17)
    }
}

dependencies {
    implementation("org.godotengine:godot:4.2.1.stable")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
}
