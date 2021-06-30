package com.keith.library

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout

class KeyboardHeaderLayout : ConstraintLayout, KeyboardHeightObserver {

    private val TAG: String = "KeyboardHeaderLayout"
    private var mKeyboardHeightProvider: KeyboardHeightProvider
    private var mMode = Mode.SHOW_WITH_KEYBOARD

    constructor(context: Context): super(context) {
        mKeyboardHeightProvider = KeyboardHeightProvider(context as Activity)
    }

    constructor(context: Context, attrs: AttributeSet?): super(context, attrs) {
        mKeyboardHeightProvider = KeyboardHeightProvider(context as Activity)
    }

    constructor(context: Context, attrs: AttributeSet?,
                defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mKeyboardHeightProvider = KeyboardHeightProvider(context as Activity)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mKeyboardHeightProvider.setKeyboardHeightObserver(this)
        mKeyboardHeightProvider.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mKeyboardHeightProvider.close()
    }

    override fun onKeyboardHeightChanged(height: Int, orientation: Int) {
        Log.i(TAG, "Keyboard height: $height")
        if (height > 0) {
            if (visibility != View.VISIBLE) {
                visibility = View.VISIBLE
            }
        } else {
            if (mMode == Mode.SHOW_WITH_KEYBOARD) {
                visibility = View.GONE
                return
            }
        }
        val layoutParams = layoutParams as MarginLayoutParams
        layoutParams.bottomMargin = height
        this.layoutParams = layoutParams
    }

    fun setDisplayMode(mode: Mode) {
        mMode = mode
    }

    /**
     * SHOW_WITH_KEYBOARD: Only show header when keyboard is shown
     * SHOW_ALWAYS: SHOW_WITH_KEYBOARD + show at screen bottom when keyboard is hidden
     */
    enum class Mode {
        SHOW_WITH_KEYBOARD,
        SHOW_ALWAYS
    }
}