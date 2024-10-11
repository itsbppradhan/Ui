package b.ui.utils

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.compose.material3.MaterialTheme
import b.ui.R

class Glass(
    private val context: Context,
    private val window: Window,
    private val backgroundBlurRadius: Int = 80, // Default blur radius
    private val blurBehindRadius: Int = 20 // Default blur behind radius
) {

    private var dimAmountWithBlur = 0.1f
    private var dimAmountNoBlur = 0.4f
    private var windowBackgroundAlphaWithBlur = 170 // 80% transparency
    private var windowBackgroundAlphaNoBlur = 255 // 100% opacity
    private var windowBackgroundDrawable: ColorDrawable? = null

    // Initialization method, call to set up the blur feature
    fun init() {
        // Set the window background drawable to transparent initially
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Enable blur effects if supported, or update window state
        window.addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND)
        setupWindowBlurListener() // Set up listener for detecting changes in blur state

        // Enable dim behind the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
    }

    // Update the window background to a Material color scheme with specified transparency
    private fun updateWindowBackground(blurEnabled: Boolean) {
        // Check if the system is in dark mode
        val isDarkMode = (context.resources.configuration.uiMode and
                android.content.res.Configuration.UI_MODE_NIGHT_MASK) ==
                android.content.res.Configuration.UI_MODE_NIGHT_YES

        // Select color based on the theme
        val color = if (isDarkMode) {
            ContextCompat.getColor(context, R.color.black) // Replace with your dark mode color resource
        } else {
            ContextCompat.getColor(context, R.color.white) // Replace with your light mode color resource
        }

        // Adjust the color for transparency
        val alpha = if (blurEnabled) windowBackgroundAlphaWithBlur else windowBackgroundAlphaNoBlur
        windowBackgroundDrawable = ColorDrawable(color).apply { this.alpha = alpha }
        window.setBackgroundDrawable(windowBackgroundDrawable)
    }

    // Private method to enable blur effect
    private fun enable() {
        window.setBackgroundBlurRadius(backgroundBlurRadius)
        window.attributes.blurBehindRadius = blurBehindRadius
        window.attributes = window.attributes
    }

    // Private method to disable blur effect
    private fun disable() {
        window.setBackgroundBlurRadius(0)
        window.attributes.blurBehindRadius = 0
        window.attributes = window.attributes
    }

    // Listener to detect blur support change and adjust UI accordingly
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
        updateWindowBackground(blursEnabled) // Call to update background color

        window.setDimAmount(if (blursEnabled) dimAmountWithBlur else dimAmountNoBlur)

        // Set the background and behind blur radii
        window.setBackgroundBlurRadius(if (blursEnabled) backgroundBlurRadius else 0)
        window.attributes.blurBehindRadius = if (blursEnabled) blurBehindRadius else 0
        window.attributes = window.attributes
    }
}
