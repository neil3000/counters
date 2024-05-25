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

package dev.rahmouni.neil.counters.core.common

import android.net.Uri
import androidx.core.net.toUri

sealed interface Rn3Uri {
    data object AndroidPreview : Rn3Uri
    data object InMaintenance : Rn3Uri
    data object Unavailable : Rn3Uri
    data object SoonAvailable : Rn3Uri
    data class Available(val androidUri: Uri, val analyticsClickEvent: (() -> Unit)?) : Rn3Uri
}

fun String.toRn3Uri(analyticsClickEvent: (() -> Unit)?): Rn3Uri {
    return when (this) {
        "DEMO_FAKE" -> Rn3Uri.AndroidPreview
        "MAINTENANCE" -> Rn3Uri.InMaintenance
        "UNAVAILABLE" -> Rn3Uri.Unavailable
        "SOON" -> Rn3Uri.SoonAvailable
        else -> Rn3Uri.Available(this.toUri(), analyticsClickEvent)
    }
}
