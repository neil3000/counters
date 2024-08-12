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
