plugins {
    id("com.android.library").apply(false)
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        maven("https://jitpack.io")
        google()
    }
}
