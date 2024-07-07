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

package dev.rahmouni.neil.counters.feature.settings.developer.main.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.feature.settings.developer.main.model.data.PreviewParameterData.developerSettingsData_default
import dev.rahmouni.neil.counters.feature.settings.developer.main.model.data.PreviewParameterData.developerSettingsData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [DeveloperSettingsData] for Composable previews.
 */
class DeveloperSettingsDataPreviewParameterProvider :
    PreviewParameterProvider<DeveloperSettingsData> {
    override val values: Sequence<DeveloperSettingsData> =
        sequenceOf(developerSettingsData_default).plus(developerSettingsData_mutations)
}

object PreviewParameterData {
    val developerSettingsData_default = DeveloperSettingsData(
        isUserAdmin = false,
    )
    val developerSettingsData_mutations = with(developerSettingsData_default) {
        sequenceOf(
            copy(isUserAdmin = true),
        )
    }
}
