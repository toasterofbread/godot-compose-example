package dev.toastbits.webviewtest.scene.main

import godot.Object as GodotObject
import godot.core.Dictionary
import godot.core.PackedByteArray
import godot.core.asStringName

class AndroidWebView(private val plugin: GodotObject) {
	fun init(width: Int, height: Int) {
		plugin.call("init".asStringName(), width, height)
	}

	fun setSize(width: Int, height: Int) {
		plugin.call("setSize".asStringName(), width, height)
	}

	fun drawToBitmap(): PackedByteArray {
		val array_container: Dictionary<*, *> = plugin.call("drawToBitmap".asStringName()) as Dictionary<*, *>
		return array_container["array"]!! as PackedByteArray
	}

	fun loadUrl(url: String) {
		plugin.call("loadUrl".asStringName(), url)
	}
}
