package dev.rahmouni.neil.counters.core.designsystem.rebased

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
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
import dev.rahmouni.neil.counters.core.designsystem.icons.Building
import dev.rahmouni.neil.counters.core.designsystem.icons.HomeGroup
import dev.rahmouni.neil.counters.core.designsystem.icons.Road

enum class SharingScope(val text: String) {
    GLOBAL(text = "Global"),
    COUNTRY(text = "Country"),
    REGION(text = "Region"),
    CITY(text = "City"),
    DISTRICT(text = "District"),
    NEIGHBORHOOD(text = "Neighborhood"),
    STREET(text = "Street"),
    BUILDING(text = "Building");

    @Composable
    fun icon(location: String): ImageVector {
        return when (this) {
            GLOBAL -> Icons.Outlined.Public
            COUNTRY -> Country.getIconFromIso(location)
            REGION -> Icons.Outlined.Landscape
            CITY -> Icons.Outlined.LocationCity
            DISTRICT -> Icons.Outlined.Bank
            NEIGHBORHOOD -> Icons.Outlined.HomeGroup
            STREET -> Icons.Outlined.Road
            BUILDING -> Icons.Outlined.Building
        }
    }

    @Composable
    fun DisplayIcon(location: String) {
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