package dev.toastbits.webviewtest.scene.main

import godot.Engine
import godot.Image
import godot.ImageTexture
import godot.Node2D
import godot.Object as GodotObject
import godot.TextureRect
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.core.PackedByteArray
import godot.core.asStringName
import godot.extensions.getNodeAs

private const val PLUGIN_NAME: String = "WebViewTestAndroidPlugin"

@RegisterClass
class Main: Node2D() {
	private val width: Int = 1080
	private val height: Int = 2220
	private val image_format: Image.Format = Image.Format.FORMAT_RGBA8

	private val texture_rect: TextureRect by lazy { getNodeAs("CanvasLayer/TextureRect")!! }
	private val image: Image = Image.create(width, height, false, image_format)!!

	private var webview: AndroidWebView? = null
	private var webview_pixels: PackedByteArray? = null

	@RegisterFunction
	override fun _ready() {
		webview = createAndroidWebview()?.apply {
			init(width, height)
			loadUrl("https://github.com/toasterofbread")
		}
	}

	@RegisterFunction
	override fun _process(delta: Double) {
		webview?.apply {
			val bytes: PackedByteArray = drawToBitmap()
			image.setData(width, height, false, image_format, bytes)
			texture_rect.texture = ImageTexture.createFromImage(image)
		}
	}

	@RegisterFunction
	fun createAndroidWebview(): AndroidWebView? {
		if (!Engine.hasSingleton(PLUGIN_NAME.asStringName())) {
			return null
		}

		val plugin: GodotObject = Engine.getSingleton(PLUGIN_NAME.asStringName())!!
		return AndroidWebView(plugin)
	}
}
