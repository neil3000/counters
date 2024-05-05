/*
 * Copyright 2024 Rahmouni Neïl
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
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.PreviewParameterData.p1
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.PreviewParameterData.p2
import dev.rahmouni.neil.counters.feature.settings.dataAndPrivacy.model.data.PreviewParameterData.p3

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [DataAndPrivacySettingsData] for Composable previews.
 */
class DataAndPrivacySettingsDataPreviewParameterProvider :
    PreviewParameterProvider<DataAndPrivacySettingsData> {
    override val values: Sequence<DataAndPrivacySettingsData> = sequenceOf(p1, p2, p3)
}

object PreviewParameterData {
    val p1 = DataAndPrivacySettingsData(
        hasMetricsEnabled = false,
        hasCrashlyticsEnabled = false,
    )
    val p2 = DataAndPrivacySettingsData(
        hasMetricsEnabled = false,
        hasCrashlyticsEnabled = true,
    )
    val p3 = DataAndPrivacySettingsData(
        hasMetricsEnabled = true,
        hasCrashlyticsEnabled = false,
    )
}
