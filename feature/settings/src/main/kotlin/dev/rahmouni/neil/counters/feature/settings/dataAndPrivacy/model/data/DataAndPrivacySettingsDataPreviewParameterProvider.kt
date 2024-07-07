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

package dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.PreviewParameterData.dataAndPrivacySettingsData_default
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.PreviewParameterData.dataAndPrivacySettingsData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [DataAndPrivacySettingsData] for Composable previews.
 */
class DataAndPrivacySettingsDataPreviewParameterProvider :
    PreviewParameterProvider<DataAndPrivacySettingsData> {
    override val values: Sequence<DataAndPrivacySettingsData> =
        sequenceOf(dataAndPrivacySettingsData_default).plus(dataAndPrivacySettingsData_mutations)
}

internal object PreviewParameterData {
    val dataAndPrivacySettingsData_default = DataAndPrivacySettingsData(
        hasMetricsEnabled = true,
        hasCrashlyticsEnabled = true,
    )
    val dataAndPrivacySettingsData_mutations = with(dataAndPrivacySettingsData_default) {
        sequenceOf(
            copy(hasMetricsEnabled = false),
            copy(hasCrashlyticsEnabled = false),
        )
    }
}
