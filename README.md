# Godot Android Webview Proof of Concept

PoC Godot project which displays an Android webview (or, in theory, any Android content) as a texture.

## Please note
- Doesn't forward input to the webview
- Doesn't work with WASM (WebGL?) content for some reason
- I'm pretty sure there's a memory leak somewhere

## Building

Build plugin: `./gradlew plugin:build`

Build application: `./gradlew app:build`
