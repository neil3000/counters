/*
 * Copyright 2024 Rahmouni Neïl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.rahmouni.neil.counters.core.designsystem.component

import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.ui.hapticfeedback.HapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalView

@Composable
fun getHaptic(): Haptic {
    return Haptic().init()
}

class Haptic {

    private var view: View? = null
    private var haptic: HapticFeedback? = null

    @Composable
    fun init(): Haptic {
        view = LocalView.current
        haptic = LocalHapticFeedback.current

        return this
    }

    fun click() {
        if (view == null || haptic == null) throw hapticClassNonInitializedException("vRzFlAB4R2BL1DgQVtDWBUjK2hxPwL79")

        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            view?.performHapticFeedback(HapticFeedbackConstants.CONTEXT_CLICK)
        } else {
            haptic?.performHapticFeedback(HapticFeedbackType.LongPress)
        }
    }

    fun smallTick() {
        if (view == null || haptic == null) throw hapticClassNonInitializedException("5ClNrZ9riFoNkR0kTjFylYmdgfmOXnm6")

        if (VERSION.SDK_INT >= VERSION_CODES.O_MR1) {
            view?.performHapticFeedback(HapticFeedbackConstants.TEXT_HANDLE_MOVE)
        } else {
            haptic?.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    fun longPress() {
        if (view == null || haptic == null) throw hapticClassNonInitializedException("cU87d2YU9jf5P2oDTNrLFlPfMHAfW7Be")

        view?.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
    }

    fun toggle(state: Boolean) {
        if (state) toggleOn() else toggleOff()
    }

    private fun toggleOn() {
        if (view == null || haptic == null) throw hapticClassNonInitializedException("2DE1Fjt7SaM5oVBgUZbaKu0T3FHy0WXQ")

        if (VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view?.performHapticFeedback(HapticFeedbackConstants.TOGGLE_ON)
        } else {
            click()
        }
    }

    private fun toggleOff() {
        if (view == null || haptic == null) throw hapticClassNonInitializedException("wSnwbynXGOaOkVsXZCRxVKAJWEmJ6iPF")

        if (VERSION.SDK_INT >= VERSION_CODES.UPSIDE_DOWN_CAKE) {
            view?.performHapticFeedback(HapticFeedbackConstants.TOGGLE_OFF)
        } else {
            click()
        }
    }

    private fun hapticClassNonInitializedException(localID: String): Exception {
        return Exception("Haptic function not initialized | $localID")
    }
}
