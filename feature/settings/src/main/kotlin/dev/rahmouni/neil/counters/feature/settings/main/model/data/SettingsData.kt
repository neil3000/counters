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

package dev.rahmouni.neil.counters.feature.settings.main.model.data

import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import dev.rahmouni.neil.counters.core.user.Rn3User

data class SettingsData(
    val user: Rn3User,
    val devSettingsEnabled: Boolean,
    val hasTravelModeEnabled: Boolean,
    val hasFriendsMainEnabled: Boolean,
    val inAppUpdateData: InAppUpdateState,
)

sealed interface InAppUpdateState {

    data object NoUpdateAvailable : InAppUpdateState
    data class UpdateAvailable(
        val appUpdateManager: AppUpdateManager?,
        val appUpdateInfo: AppUpdateInfo?,
    ) : InAppUpdateState

    data class DownloadingUpdate(val progress: Float) : InAppUpdateState
    data class WaitingForRestart(val appUpdateManager: AppUpdateManager?) : InAppUpdateState

    fun shouldShowTile() = this != NoUpdateAvailable
    fun actionPossible() = this is UpdateAvailable || this is WaitingForRestart
}
