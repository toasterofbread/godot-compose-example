plugins {
    kotlin("android")
    id("com.android.library")
    id("org.jetbrains.compose")
}

kotlin {
    jvmToolchain(17)
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "dev.toastbits.godotcomposeexample.plugin"

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

    implementation(compose.runtime)
    implementation(compose.foundation)
    // implementation(compose.material)
    implementation(compose.material3)
    // implementation(compose.materialIconsExtended)
    // implementation(compose.components.resources)
}
