
# Jetpack Compose in Godot (example project)

PoC Godot project which uses an [Android plugin](https://docs.godotengine.org/en/stable/tutorials/platform/android/android_plugin.html) to display native Jetpack Compose content. Also forwards touch events applied onto the Godot node to the Android view.

The Android view is displayed in Godot by drawing it onto a bitmap, then converting that to an array of pixel bytes and passing that back to Godot and displaying it as an ImageTexture using a TextureRect node. This method is sort of hacky and doesn't seem to work with hardware acceleration, but achieves somewhat usable results.

## Building

1. Build plugin: `./gradlew plugin:build`
2. Install Android build template (project tab)
3. Build Android project
