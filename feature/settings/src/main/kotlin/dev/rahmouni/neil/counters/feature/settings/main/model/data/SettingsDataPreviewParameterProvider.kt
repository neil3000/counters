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

package dev.rahmouni.neil.counters.feature.settings.main.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.LoggedOutUser
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.SignedInUser
import dev.rahmouni.neil.counters.feature.settings.main.model.data.PreviewParameterData.settingsData_default
import dev.rahmouni.neil.counters.feature.settings.main.model.data.PreviewParameterData.settingsData_devSettingsEnabled
import dev.rahmouni.neil.counters.feature.settings.main.model.data.PreviewParameterData.settingsData_loggedOut

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [SettingsData] for Composable previews.
 */
class SettingsDataPreviewParameterProvider :
    PreviewParameterProvider<SettingsData> {
    override val values: Sequence<SettingsData> = sequenceOf(
        settingsData_default, settingsData_loggedOut, settingsData_devSettingsEnabled,
    )
}

object PreviewParameterData {
    val settingsData_default = SettingsData(
        user = SignedInUser(
            displayName = "Android Preview",
            pfpUri = null,
        ),
        devSettingsEnabled = false,
    )
    val settingsData_loggedOut =
        settingsData_default.copy(
            user = LoggedOutUser,
        )
    val settingsData_devSettingsEnabled =
        settingsData_default.copy(
            devSettingsEnabled = true,
        )
}
