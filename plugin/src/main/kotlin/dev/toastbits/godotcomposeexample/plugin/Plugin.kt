package dev.toastbits.godotcomposeexample.plugin

import org.godotengine.godot.Godot
import org.godotengine.godot.plugin.GodotPlugin
import org.godotengine.godot.plugin.UsedByGodot
import org.godotengine.godot.Dictionary
import android.view.ViewGroup
import android.view.View
import android.app.Activity
import android.widget.RelativeLayout
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.suspendCoroutine

class Plugin(godot: Godot): GodotPlugin(godot) {
    private val android_views: MutableList<AndroidView?> = mutableListOf()

    override fun getPluginName(): String = "ComposeAndroidPlugin"

    override fun onMainDestroy() {
        for (view in android_views) {
            view?.release()
        }
    }

    @UsedByGodot
    fun createView(width: Int, height: Int): Int = onUiThreadBlocking {
        val activity: Activity = getActivity()!!
        val view: AndroidView =
            AndroidView(
                activity,
                width,
                height,
                onUiThread = ::onUiThread
            )

        activity.window.addContentView(view.view, ViewGroup.MarginLayoutParams(width, height))
        view.view.setVisibility(View.INVISIBLE)
        android_views.add(view)

        return@onUiThreadBlocking android_views.size - 1
    }

    @UsedByGodot
    fun deleteView(view_index: Int) {
        val view: AndroidView = android_views.getOrNull(view_index) ?: return

        onUiThread {
            android_views.removeAt(view_index )
            (view.view.getParent() as? ViewGroup)?.removeView(view.view)
            view.release()
        }
    }

    @UsedByGodot
    fun enableView(view_index: Int, draw_data: String? = null) {
        getView(view_index)?.enable(draw_data)
    }

    @UsedByGodot
    fun disableView(view_index: Int) {
        getView(view_index)?.disable()
    }

    @UsedByGodot
    fun viewSetSize(view_index: Int, width: Int, height: Int) {
        getView(view_index)?.setSize(width, height)
    }

    @UsedByGodot
    fun viewDraw(view_index: Int): Dictionary {
        val view: AndroidView = getView(view_index) ?: return Dictionary()
        // onUiThread {
        //     view.draw(data)
        // }

        return view.getDrawByteArray()
    }

    @UsedByGodot
    fun viewOnTouchEvent(
        view_index: Int,
        button_mask: Long,
        x: Float,
        y: Float
    ) {
        val view: AndroidView = getView(view_index) ?: return
        view.onTouchEvent(button_mask, x, y)
    }

    private fun getView(view_index: Int): AndroidView? =
        android_views.getOrNull(view_index)

    fun onUiThread(action: () -> Unit) {
        runOnUiThread(action)
    }

    fun <T> onUiThreadBlocking(action: () -> T): T =
        runBlocking {
            suspendCoroutine { continuation ->
                runOnUiThread {
                    val result: Result<T> =
                        runCatching {
                            action()
                        }

                    continuation.resumeWith(result)
                }
            }
        }
}
