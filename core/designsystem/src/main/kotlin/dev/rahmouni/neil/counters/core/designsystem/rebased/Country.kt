package dev.rahmouni.neil.counters.core.designsystem.rebased

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.rahmouni.neil.counters.core.designsystem.icons.Belgium
import dev.rahmouni.neil.counters.core.designsystem.icons.France
import dev.rahmouni.neil.counters.core.designsystem.icons.UK
import dev.rahmouni.neil.counters.core.designsystem.icons.USA

enum class Country(val text: String, val isoCode: String) {
    FRANCE("France", "FR"),
    BELGIUM("Belgium", "BE"),
    UNITED_KINGDOM("United Kingdom", "GB"),
    USA("United States", "US");

    companion object {
        private val isoMap = entries.associateBy(Country::isoCode)

        @Composable
        fun getCountryIcon(isoCode: String): ImageVector {
            val country = isoMap[isoCode]
            return when (country) {
                FRANCE -> Icons.Outlined.France
                BELGIUM -> Icons.Outlined.Belgium
                UNITED_KINGDOM -> Icons.Outlined.UK
                USA -> Icons.Outlined.USA
                else -> Icons.Outlined.Flag
            }
        }

        fun getCountryNameFromIso(isoCode: String): String {
            return isoMap[isoCode]?.text ?: "Country not found"
        }
    }
}