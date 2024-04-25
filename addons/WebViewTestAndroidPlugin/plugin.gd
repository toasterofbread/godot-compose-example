@tool
extends EditorPlugin

var instance: WebViewTestAndroidPlugin

func _enter_tree():
	instance = WebViewTestAndroidPlugin.new()
	add_export_plugin(instance)

func _exit_tree():
	remove_export_plugin(instance)
	instance = null

class WebViewTestAndroidPlugin extends EditorExportPlugin:
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
		return PackedStringArray([])

	func _get_android_dependencies_maven_repos(platform, debug):
		return PackedStringArray([])

	func _get_name():
		return "WebViewTestAndroidPlugin"
