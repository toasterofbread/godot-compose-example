import org.apache.commons.io.FileUtils

plugins {
    id("com.utopia-rise.godot-kotlin-jvm")
}

godot {
    isAndroidExportEnabled.set(true)
    androidCompileSdkDir.set(File("/opt/android-sdk/platforms/android-34"))
    d8ToolPath.set(File("/opt/android-sdk/build-tools/34.0.0/d8"))
    androidMinApi.set((extra["android.minSdk"] as String).toInt())
}

afterEvaluate {
    tasks.getByName("build").finalizedBy(
        tasks.register("copyLibs") {
            doFirst {
                FileUtils.copyDirectory(project.file("build/libs"), rootProject.file("build/libs"))
            }
        }
    )
}
