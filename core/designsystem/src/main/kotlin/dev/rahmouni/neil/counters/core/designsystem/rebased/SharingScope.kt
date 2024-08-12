/*
 * Copyright (C) 2024 Rahmouni Neïl & Aurélien Coppée
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

package dev.rahmouni.neil.counters.core.designsystem.rebased

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
import dev.rahmouni.neil.counters.core.designsystem.R
import dev.rahmouni.neil.counters.core.designsystem.icons.Bank
import dev.rahmouni.neil.counters.core.designsystem.icons.Building
import dev.rahmouni.neil.counters.core.designsystem.icons.HomeGroup
import dev.rahmouni.neil.counters.core.designsystem.icons.Road
import dev.rahmouni.neil.counters.core.model.data.Country
import java.util.Locale

enum class SharingScope(private val resourceId: Int) {
    GLOBAL(R.string.core_designsystem_sharingScope_global),
    COUNTRY(R.string.core_designsystem_sharingScope_country),
    REGION(R.string.core_designsystem_sharingScope_region),
    CITY(R.string.core_designsystem_sharingScope_city),
    DISTRICT(R.string.core_designsystem_sharingScope_district),
    NEIGHBORHOOD(R.string.core_designsystem_sharingScope_neighborhood),
    STREET(R.string.core_designsystem_sharingScope_street),
    BUILDING(R.string.core_designsystem_sharingScope_building);

    @Composable
    fun text(): String {
        return stringResource(id = resourceId)
    }

    @Composable
    fun icon(location: String?): ImageVector {
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
    fun DisplayIcon(location: String?) {
        Icon(
            imageVector = icon(location),
            modifier = Modifier
                .size(34.dp)
                .clip(CircleShape)
                .padding(2.dp),
            contentDescription = null,
            tint = if (this == COUNTRY) Color.Unspecified else MaterialTheme.colorScheme.onPrimaryContainer,
        )
    }

    companion object {
        fun fromString(value: String?): SharingScope {
            return when (value?.uppercase(Locale.ROOT)) {
                "GLOBAL" -> GLOBAL
                "COUNTRY" -> COUNTRY
                "REGION" -> REGION
                "CITY" -> CITY
                "DISTRICT" -> DISTRICT
                "NEIGHBORHOOD" -> NEIGHBORHOOD
                "STREET" -> STREET
                "BUILDING" -> BUILDING
                else -> BUILDING
            }
        }
    }
}
