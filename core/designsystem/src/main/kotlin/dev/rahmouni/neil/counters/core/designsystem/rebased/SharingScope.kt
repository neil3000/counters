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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.R.string
import dev.rahmouni.neil.counters.core.designsystem.icons.Bank
import dev.rahmouni.neil.counters.core.designsystem.icons.Building
import dev.rahmouni.neil.counters.core.designsystem.icons.HomeGroup
import dev.rahmouni.neil.counters.core.designsystem.icons.Road
import dev.rahmouni.neil.counters.core.model.data.Country

enum class SharingScope(private val resourceId: Int) {
    GLOBAL(string.core_designsystem_sharingScope_global),
    COUNTRY(string.core_designsystem_sharingScope_country),
    REGION(string.core_designsystem_sharingScope_region),
    CITY(string.core_designsystem_sharingScope_city),
    DISTRICT(string.core_designsystem_sharingScope_district),
    NEIGHBORHOOD(string.core_designsystem_sharingScope_neighborhood),
    STREET(string.core_designsystem_sharingScope_street),
    BUILDING(string.core_designsystem_sharingScope_building);

    @Composable
    fun text(): String {
        return stringResource(id = resourceId)
    }

    @Composable
    fun icon(location: String): ImageVector {
        return when (this) {
            GLOBAL -> Icons.Outlined.Public
            COUNTRY -> Country.getCountryFromIso(location)?.icon() ?: Icons.Outlined.Flag
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
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(6.dp),
            contentDescription = null,
            tint = if (this == COUNTRY) Color.Unspecified else MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }
}