/*
 * This file is part of Siebe Projects samples.
 *
 * Siebe Projects samples is free software: you can redistribute it and/or modify
 * it under the terms of the Lesser GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Siebe Projects samples is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Lesser GNU General Public License for more details.
 *
 * You should have received a copy of the Lesser GNU General Public License
 * along with Siebe Projects samples.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.keith.library

import android.app.Activity
import android.content.res.Configuration
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager.LayoutParams
import android.widget.PopupWindow

/**
 * The keyboard height provider, this class uses a PopupWindow
 * to calculate the window height when the floating keyboard is opened and closed.
 */
class KeyboardHeightProvider(private val activity: Activity) : PopupWindow(activity) {

  /** The keyboard height observer  */
  private var observer: KeyboardHeightObserver? = null

  /** The cached landscape height of the keyboard  */
  private var keyboardLandscapeHeight = 0

  /** The cached portrait height of the keyboard  */
  private var keyboardPortraitHeight = 0

  /** The view that is used to calculate the keyboard height  */
  private var popupView: View? = null

  /** The parent view  */
  private var parentView: View? = null

  private var lastY = 0

  init {
    val inflater = activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    popupView = inflater.inflate(R.layout.popupwindow, null, false)
    contentView = popupView
    softInputMode = LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
        LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE
    inputMethodMode = INPUT_METHOD_NEEDED
    parentView = activity.findViewById(android.R.id.content)
    width = 0
    height = LayoutParams.MATCH_PARENT
    popupView?.viewTreeObserver?.addOnGlobalLayoutListener {
      if (popupView != null) {
        handleOnGlobalLayout()
      }
    }
  }

  /**
   * Start the KeyboardHeightProvider, this must be called after the onResume of the Activity.
   * PopupWindows are not allowed to be registered before the onResume has finished
   * of the Activity.
   */
  fun start() {
    if (!isShowing && parentView?.windowToken != null) {
      setBackgroundDrawable(ColorDrawable(0))
      showAtLocation(parentView, Gravity.NO_GRAVITY, 0, 0)
    }
  }

  /**
   * Close the keyboard height provider,
   * this provider will not be used anymore.
   */
  fun close() {
    observer = null
    dismiss()
  }

  /**
   * Set the keyboard height observer to this provider. The
   * observer will be notified when the keyboard height has changed.
   * For example when the keyboard is opened or closed.
   *
   * @param observer The observer to be added to this provider.
   */
  fun setKeyboardHeightObserver(observer: KeyboardHeightObserver?) {
    this.observer = observer
  }

  /**
   * Popup window itself is as big as the window of the Activity.
   * The keyboard can then be calculated by extracting the popup view bottom
   * from the activity window height.
   */
  private fun handleOnGlobalLayout() {
    val rect = Rect()
    popupView?.getWindowVisibleDisplayFrame(rect)

    val orientation = getScreenOrientation()
    if (lastY == 0) {
      lastY = rect.bottom
    }
    val keyboardHeight: Int = lastY - rect.bottom
    if (keyboardHeight > 0) {
      if (orientation == Configuration.ORIENTATION_PORTRAIT) {
        keyboardPortraitHeight = keyboardHeight
      } else {
        keyboardLandscapeHeight = keyboardHeight
      }
    }
    notifyKeyboardHeightChanged(keyboardHeight, orientation)
  }

  private fun getScreenOrientation(): Int {
    return activity.resources.configuration.orientation
  }

  private fun notifyKeyboardHeightChanged(height: Int, orientation: Int) {
    observer?.onKeyboardHeightChanged(height, orientation)
  }
}