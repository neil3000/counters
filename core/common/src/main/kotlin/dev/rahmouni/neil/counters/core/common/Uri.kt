/*
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
