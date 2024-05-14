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

package dev.rahmouni.neil.counters.feature.settings.accessibility.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.PreviewParameterData.accessibilitySettingsData_default
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.PreviewParameterData.accessibilitySettingsData_emphasizedSwitchesEnabled
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.PreviewParameterData.accessibilitySettingsData_iconTooltipsDisabled

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [AccessibilitySettingsData] for Composable previews.
 */
class AccessibilitySettingsDataPreviewParameterProvider :
    PreviewParameterProvider<AccessibilitySettingsData> {
    override val values: Sequence<AccessibilitySettingsData> = sequenceOf(
        accessibilitySettingsData_default,
        accessibilitySettingsData_emphasizedSwitchesEnabled,
        accessibilitySettingsData_iconTooltipsDisabled,
    )
}

object PreviewParameterData {
    val accessibilitySettingsData_default = AccessibilitySettingsData(
        hasEmphasizedSwitchesEnabled = false,
        hasIconTooltipsEnabled = true,
    )
    val accessibilitySettingsData_emphasizedSwitchesEnabled =
        accessibilitySettingsData_default.copy(
            hasEmphasizedSwitchesEnabled = true,
        )
    val accessibilitySettingsData_iconTooltipsDisabled = accessibilitySettingsData_default.copy(
        hasIconTooltipsEnabled = false,
    )
}
