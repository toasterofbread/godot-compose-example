package dev.toastbits.godotcomposeexample.plugin

import android.view.View
import android.os.SystemClock
import android.view.MotionEvent

private const val PRECISION: Float = Float.MIN_VALUE

class InputLayer(val view: View) {
    private val motion_down_times: MutableMap<MouseButtonMask, Long?> = mutableMapOf()

    fun onTouchEvent(button_mask: Long, x: Float, y: Float) {
        val time: Long = SystemClock.uptimeMillis()

        for (button in MouseButtonMask.entries) {
            val pressed: Boolean = (button_mask and button_mask) != 0L
            var down_time: Long? = motion_down_times[button]

            val action: Int

            if (pressed) {
                if (down_time == null) {
                    down_time = time
                    motion_down_times[button] = down_time
                    action = MotionEvent.ACTION_DOWN
                }
                else {
                    action = MotionEvent.ACTION_MOVE
                }
            }
            else if (down_time != null) {
                motion_down_times[button] = null
                action = MotionEvent.ACTION_UP
            }
            else {
                continue
            }

            val event: MotionEvent =
                MotionEvent.obtain(
                    down_time, // downTime
                    time, // eventTime
                    action, // action
                    x, // x
                    y, // y
                    1f, // pressure
                    1f, // size
                    0, // metaState
                    PRECISION, // xPrecision
                    PRECISION, // yPrecision
                    0, // deviceId
                    0 // edgeFlags
                )

            view.dispatchTouchEvent(event)
        }
    }
}

// https://docs.godotengine.org/en/stable/classes/class_%40globalscope.html#enum-globalscope-mousebuttonmask
enum class MouseButtonMask(val value: Long) {
    MOUSE_BUTTON_MASK_LEFT(1),
    MOUSE_BUTTON_MASK_RIGHT(2),
    MOUSE_BUTTON_MASK_MIDDLE(4),
    MOUSE_BUTTON_MASK_MB_XBUTTON1(128),
    MOUSE_BUTTON_MASK_MB_XBUTTON2(256)
}
