package dev.toastbits.webviewtest.plugin

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.view.View
import android.webkit.*
import android.widget.RelativeLayout
import java.nio.ByteBuffer
import kotlin.coroutines.suspendCoroutine
import kotlinx.coroutines.runBlocking
import org.godotengine.godot.Dictionary
import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.UsedByGodot

class Plugin(godot: Godot): GodotPlugin(godot) {
    override fun getPluginName(): String = "WebViewTestAndroidPlugin"

    private lateinit var webview: WebView
    private lateinit var draw_bitmap: Bitmap
    private lateinit var draw_canvas: Canvas
    private lateinit var pixel_buffer: ByteBuffer
    private lateinit var pixel_out_array: ByteArray
    private val dictionary: Dictionary = Dictionary()

    @UsedByGodot
    fun init(width: Int, height: Int) {
        runBlocking {
            suspendCoroutine { continuation ->
                runOnUiThread {
                    val result: Result<Unit> =
                        runCatching {
                            webview = createWebview()
                            setSize(width, height)
                        }

                    continuation.resumeWith(result)
                }
            }
        }
    }

    @UsedByGodot
    fun setCookie(url: String, key: String, value: String) {
        CookieManager.getInstance().setCookie(url, "$key=$value")
    }

    @UsedByGodot
    fun getCookies(url: String): String =
        CookieManager.getInstance().getCookie(url)

    @UsedByGodot
    fun setSize(width: Int, height: Int) {
        val params: RelativeLayout.LayoutParams = RelativeLayout.LayoutParams(width, height)
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
        webview.layoutParams = params

        draw_bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        draw_canvas = Canvas(draw_bitmap)

        val byte_count: Int = draw_bitmap.getAllocationByteCount()
        pixel_buffer = ByteBuffer.allocate(byte_count)
        pixel_out_array = ByteArray(byte_count)
    }

    @UsedByGodot
    fun drawToBitmap(): Dictionary {
        webview.layout(0, 0, draw_bitmap.width, draw_bitmap.height)

        webview.isDrawingCacheEnabled = true
        webview.buildDrawingCache()

        draw_canvas.drawBitmap(draw_bitmap, 0f, draw_bitmap.height.toFloat(), Paint())
        webview.draw(draw_canvas)
        draw_bitmap.copyPixelsToBuffer(pixel_buffer)
        pixel_buffer.rewind()

        pixel_buffer.get(pixel_out_array, 0, pixel_out_array.size)
        pixel_buffer.rewind()

        dictionary["array"] = pixel_out_array
        return dictionary
    }

    @UsedByGodot
    fun loadUrl(url: String) {
        try {
            runOnUiThread {
                webview.loadUrl(url)
            }
        }
        catch (e: Throwable) {
            e.printStackTrace()
            throw e
        }
    }

    private fun createWebview(): WebView {
        WebView.enableSlowWholeDocumentDraw()

        val context: Context = getActivity()!!.getApplicationContext()
        return WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.useWideViewPort = true
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = true
            settings.allowFileAccess = true
            settings.allowContentAccess = true
            settings.allowFileAccessFromFileURLs = true
            settings.allowUniversalAccessFromFileURLs = true

            // webViewClient = object : WebViewClient() {
            //     override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            //     }
            //     override fun onPageFinished(view: WebView?, url: String?) {
            //     }
            //     override fun onReceivedError(view: WebView?, request: WebResourceRequest?, error: WebResourceError?) {
            //     }
            //     override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            //     }
            //     override fun shouldInterceptRequest(view: WebView?, request: WebResourceRequest?): WebResourceResponse? {
            //     }
            // }
        }
    }
}
