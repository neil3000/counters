package dev.rahmouni.neil.counters.feature.aboutme.model.data

import android.net.Uri
import androidx.core.net.toUri

sealed interface PortfolioState {
    data object InMaintenance : PortfolioState
    data object SoonAvailable : PortfolioState
    data class Available(val uri: Uri) : PortfolioState

    companion object {
        fun getFromConfigString(configValue: String): PortfolioState {
            return when (configValue) {
                "MAINTENANCE" -> InMaintenance
                "SOON" -> SoonAvailable
                else -> Available(configValue.toUri())
            }
        }
    }
}