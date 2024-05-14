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
        "DEMO_ONLY" -> Rn3Uri.AndroidPreview
        "MAINTENANCE" -> Rn3Uri.InMaintenance
        "UNAVAILABLE" -> Rn3Uri.SoonAvailable
        "SOON" -> Rn3Uri.SoonAvailable
        else -> Rn3Uri.Available(this.toUri(), analyticsClickEvent)
    }
}