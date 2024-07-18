package dev.rahmouni.neil.counters.core.designsystem.rebased

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.LocationCity
import androidx.compose.material.icons.outlined.Public
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.icons.Bank
import dev.rahmouni.neil.counters.core.designsystem.icons.Belgium
import dev.rahmouni.neil.counters.core.designsystem.icons.Building
import dev.rahmouni.neil.counters.core.designsystem.icons.France
import dev.rahmouni.neil.counters.core.designsystem.icons.HomeGroup
import dev.rahmouni.neil.counters.core.designsystem.icons.Road
import dev.rahmouni.neil.counters.core.designsystem.icons.UK
import dev.rahmouni.neil.counters.core.designsystem.icons.USA

enum class SharingScope {
    GLOBAL,
    COUNTRY,
    REGION,
    CITY,
    DISTRICT,
    NEIGHBORHOOD,
    STREET,
    BUILDING;

    @Composable
    fun icon(location: String? = null): ImageVector {
        return when (this) {
            GLOBAL -> Icons.Outlined.Public
            COUNTRY -> countryIcon(location)
            REGION -> Icons.Outlined.Landscape
            CITY -> Icons.Outlined.LocationCity
            DISTRICT -> Icons.Outlined.Bank
            NEIGHBORHOOD -> Icons.Outlined.HomeGroup
            STREET -> Icons.Outlined.Road
            BUILDING -> Icons.Outlined.Building
        }
    }

    @Composable
    private fun countryIcon(location: String?): ImageVector {
        return when (location) {
            "BE" -> Icons.Outlined.Belgium
            "FR" -> Icons.Outlined.France
            "UK" -> Icons.Outlined.UK
            "US" -> Icons.Outlined.USA
            else -> Icons.Outlined.Flag
        }
    }

    @Composable
    fun DisplayIcon(location: String? = null) {
        Icon(
            imageVector = icon(location),
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .padding(6.dp),
            contentDescription = null,
            tint = if (this == COUNTRY) Color.Unspecified else MaterialTheme.colorScheme.onPrimary,
        )
    }
}