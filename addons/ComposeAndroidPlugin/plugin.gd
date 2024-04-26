@tool
extends EditorPlugin

var instance: ComposeAndroidPlugin

func _enter_tree():
	instance = ComposeAndroidPlugin.new()
	add_export_plugin(instance)

func _exit_tree():
	remove_export_plugin(instance)
	instance = null

class ComposeAndroidPlugin extends EditorExportPlugin:
	func _supports_platform(platform):
		if platform is EditorExportPlatformAndroid:
			return true
		return false

	func _get_android_libraries(platform, debug):
		if debug:
			return PackedStringArray(["../plugin/build/outputs/aar/plugin-debug.aar"])
		else:
			return PackedStringArray(["../plugin/build/outputs/aar/plugin-release.aar"])

	func _get_android_dependencies(platform, debug):
		var compose_version: String = "1.6.1"
		return PackedStringArray([
			"org.jetbrains.compose.runtime:runtime:" + compose_version,
			"org.jetbrains.compose.foundation:foundation:" + compose_version,
			"org.jetbrains.compose.material3:material3:" + compose_version
		])

	func _get_android_dependencies_maven_repos(platform, debug):
		return PackedStringArray([])

	func _get_name():
		return "ComposeAndroidPlugin"
