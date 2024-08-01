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

package dev.rahmouni.neil.counters.core.designsystem.rebased

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.rahmouni.neil.counters.core.designsystem.icons.Belgium
import dev.rahmouni.neil.counters.core.designsystem.icons.France
import dev.rahmouni.neil.counters.core.designsystem.icons.UK
import dev.rahmouni.neil.counters.core.designsystem.icons.USA
import dev.rahmouni.neil.counters.core.model.data.Country

@Composable
fun Country.getIcon(): ImageVector {
    return when (this) {
        Country.FRANCE -> Icons.Outlined.France
        Country.BELGIUM -> Icons.Outlined.Belgium
        Country.UNITED_KINGDOM -> Icons.Outlined.UK
        Country.USA -> Icons.Outlined.USA
    }
}

@Composable
fun Country.Companion.getIcon(country: Country): ImageVector {
    return country.getIcon()
}

fun Country.Companion.getCountryFromIso(isoCode: String): Country? {
    return isoMap[isoCode]
}
