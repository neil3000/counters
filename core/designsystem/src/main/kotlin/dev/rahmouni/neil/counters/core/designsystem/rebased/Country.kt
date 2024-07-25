package dev.rahmouni.neil.counters.core.designsystem.rebased

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.rahmouni.neil.counters.core.designsystem.icons.Belgium
import dev.rahmouni.neil.counters.core.designsystem.icons.France
import dev.rahmouni.neil.counters.core.designsystem.icons.UK
import dev.rahmouni.neil.counters.core.designsystem.icons.USA

enum class Country(val text: String, val isoCode: String, val phoneCode: Int) {
    FRANCE(text = "France", isoCode = "FR", phoneCode = 33),
    BELGIUM(text = "Belgium", isoCode = "BE", phoneCode = 32),
    UNITED_KINGDOM(text = "United Kingdom", isoCode = "GB", phoneCode = 44),
    USA(text = "United States", isoCode = "US", phoneCode = 1);

    @Composable
    fun getIcon(): ImageVector {
        return when (this) {
            FRANCE -> Icons.Outlined.France
            BELGIUM -> Icons.Outlined.Belgium
            UNITED_KINGDOM -> Icons.Outlined.UK
            USA -> Icons.Outlined.USA
        }
    }

    companion object {
        private val isoMap = entries.associateBy(Country::isoCode)

        @Composable
        fun getIcon(country: Country): ImageVector {
            return country.getIcon()
        }

        @Composable
        fun getIconFromIso(isoCode: String): ImageVector {
            val country = entries.find { it.isoCode == isoCode }
            return country?.getIcon() ?: Icons.Outlined.Flag
        }

        fun getNameFromIso(isoCode: String): String {
            return isoMap[isoCode]?.text ?: "Country not found"
        }
    }
}