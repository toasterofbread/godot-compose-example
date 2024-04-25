plugins {
    id("com.utopia-rise.godot-kotlin-jvm").apply(false)
    id("com.android.library").apply(false)
}

allprojects {
    repositories {
        mavenLocal()
        maven("https://maven.mozilla.org/maven2/")
        mavenCentral()
        google()
    }
}
