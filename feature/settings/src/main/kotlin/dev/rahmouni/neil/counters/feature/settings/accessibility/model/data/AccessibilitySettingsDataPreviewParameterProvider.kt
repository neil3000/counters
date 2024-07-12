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

package dev.rahmouni.neil.counters.feature.settings.accessibility.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.PreviewParameterData.accessibilitySettingsData_default
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.PreviewParameterData.accessibilitySettingsData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [AccessibilitySettingsData] for Composable previews.
 */
class AccessibilitySettingsDataPreviewParameterProvider :
    PreviewParameterProvider<AccessibilitySettingsData> {
    override val values: Sequence<AccessibilitySettingsData> =
        sequenceOf(accessibilitySettingsData_default).plus(accessibilitySettingsData_mutations)
}

internal object PreviewParameterData {
    val accessibilitySettingsData_default = AccessibilitySettingsData(
        hasEmphasizedSwitchesEnabled = false,
        hasIconTooltipsEnabled = true,
        hasAltTextEnabled = false,
    )
    val accessibilitySettingsData_mutations = with(accessibilitySettingsData_default) {
        sequenceOf(
            copy(hasEmphasizedSwitchesEnabled = true),
            copy(hasIconTooltipsEnabled = false),
            copy(hasAltTextEnabled = true),
        )
    }
}
