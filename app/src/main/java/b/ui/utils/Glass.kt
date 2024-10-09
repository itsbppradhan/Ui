package b.ui.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat

class Glass(
    private val context: Context,
    private val window: Window,
    private val backgroundBlurRadius: Int = 80, // Default blur radius
    private val blurBehindRadius: Int = 20 // Default blur behind radius
) {

    private var dimAmountWithBlur = 0.1f
    private var dimAmountNoBlur = 0.4f
    private var windowBackgroundAlphaWithBlur = 170
    private var windowBackgroundAlphaNoBlur = 255
    private var windowBackgroundDrawable: Drawable? = null

    // Initialization method, call to set up the blur feature
    fun init() {
        // Set the window background drawable
        window.setBackgroundDrawableResource(android.R.color.transparent)

        // Enable blur effects if supported, or update window state
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
            setupWindowBlurListener() // Set up listener for detecting changes in blur state
        } else {
            updateWindowForBlurs(false) // No blur support pre-Android S
        }

        // Enable dim behind the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    // Private method to enable blur effect
    private fun enable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setBackgroundBlurRadius(backgroundBlurRadius)
            window.attributes.blurBehindRadius = blurBehindRadius
            window.attributes = window.attributes
        }
    }

    // Private method to disable blur effect
    private fun disable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            window.setBackgroundBlurRadius(0)
            window.attributes.blurBehindRadius = 0
            window.attributes = window.attributes
        }
    }

    // Listener to detect blur support change and adjust UI accordingly
    @RequiresApi(Build.VERSION_CODES.S)
    private fun setupWindowBlurListener() {
        val windowBlurEnabledListener: (Boolean) -> Unit = { blursEnabled ->
            updateWindowForBlurs(blursEnabled)
        }
        window.decorView.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View) {
                window.windowManager.addCrossWindowBlurEnabledListener(windowBlurEnabledListener)
            }

            override fun onViewDetachedFromWindow(v: View) {
                window.windowManager.removeCrossWindowBlurEnabledListener(windowBlurEnabledListener)
            }
        })
    }

    // Update the window state based on whether blur is enabled
    private fun updateWindowForBlurs(blursEnabled: Boolean) {
        windowBackgroundDrawable?.alpha = if (blursEnabled) {
            windowBackgroundAlphaWithBlur
        } else {
            windowBackgroundAlphaNoBlur
        }

        window.setDimAmount(if (blursEnabled) dimAmountWithBlur else dimAmountNoBlur)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Set the background and behind blur radii
            window.setBackgroundBlurRadius(if (blursEnabled) backgroundBlurRadius else 0)
            window.attributes.blurBehindRadius = if (blursEnabled) blurBehindRadius else 0
            window.attributes = window.attributes
        }
    }
}
