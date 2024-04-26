package dev.toastbits.godotcomposeexample.plugin

import android.view.View
import android.view.ViewGroup
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PorterDuff
import android.graphics.Paint
import android.app.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.Dispatchers
import java.nio.ByteBuffer
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import org.godotengine.godot.Dictionary
import kotlin.coroutines.suspendCoroutine

class AndroidView(
    activity: Activity,
    width: Int,
    height: Int,
    val onUiThread: (() -> Unit) -> Unit
) {
    internal val view: View
    private val input: InputLayer

    private val coroutine_scope: CoroutineScope = CoroutineScope(Job())
    private var draw_job: Job? = null

    private lateinit var draw_bitmap: Bitmap
    private lateinit var draw_canvas: Canvas
    private lateinit var draw_buffer: ByteBuffer
    private lateinit var draw_byte_array: ByteArray
    private val draw_byte_array_dict: Dictionary = Dictionary()
    private var draw_data: String? by mutableStateOf(null)

    init {
        view = ComposeView(activity.getApplicationContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                AndroidViewContent(activity, draw_data, Modifier.fillMaxSize())
            }
        }
        view.layoutParams = ViewGroup.MarginLayoutParams(width, height)

        setSize(width, height)

        input = InputLayer(view)
    }

    fun release() {
        coroutine_scope.cancel()
    }

    fun setSize(width: Int, height: Int) {
        view.layoutParams.width = width
        view.layoutParams.height = height

        draw_bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        draw_canvas = Canvas(draw_bitmap)

        val size: Int = draw_bitmap.getAllocationByteCount()
        draw_buffer = ByteBuffer.allocate(size)
        draw_byte_array = ByteArray(size)

        draw_byte_array_dict["data"] = draw_byte_array
    }

    fun enable(draw_data: String? = null) {
        this.draw_data = draw_data

        if (draw_job != null) {
            return
        }

        draw_job = coroutine_scope.launch(Dispatchers.IO) {
            while (true) {
                suspendCoroutine { continuation ->
                    onUiThread {
                        continuation.resumeWith(
                            runCatching {
                                draw()
                            }
                        )
                    }
                }
            }
        }
    }

    fun disable() {
        draw_job?.cancel()
        draw_job = null
    }

    private fun draw() {
        view.layout(0, 0, draw_bitmap.width, draw_bitmap.height)

        // view.isDrawingCacheEnabled = true
        // view.buildDrawingCache()

        draw_canvas.drawColor(0x00000000, PorterDuff.Mode.CLEAR)
        draw_canvas.drawBitmap(draw_bitmap, 50f, draw_bitmap.height.toFloat(), Paint())
        view.draw(draw_canvas)

        draw_bitmap.copyPixelsToBuffer(draw_buffer)
        draw_buffer.rewind()

        draw_buffer.get(draw_byte_array, 0, draw_byte_array.size)
        draw_buffer.rewind()
    }

    fun onTouchEvent(button_mask: Long, x: Float, y: Float) {
        input.onTouchEvent(button_mask, x, y)
    }

    fun getDrawByteArray(): Dictionary =
        draw_byte_array_dict
}
