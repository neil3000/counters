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

package dev.rahmouni.neil.counters.feature.settings.main.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.data.repository.UserDataRepository
import dev.rahmouni.neil.counters.feature.settings.main.model.SettingsUiState.Success
import dev.rahmouni.neil.counters.feature.settings.main.model.data.SettingsData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    authHelper: AuthHelper,
) : ViewModel() {

    private var devSettingsEnabled = false

    private val userDataFlow = userDataRepository.userData

    val uiState: StateFlow<SettingsUiState> =
        authHelper.getUserFlow(this.viewModelScope)
            .combine(userDataFlow) { user, userData ->
                Success(
                    SettingsData(
                        user = user,
                        hasSyncDisabled = userData.hasSyncDisabled,
                        devSettingsEnabled = devSettingsEnabled,
                    ),
                )
            }.stateIn(
                scope = viewModelScope,
                initialValue = Success(
                    SettingsData(
                        user = authHelper.getUser(),
                        hasSyncDisabled = false,
                        devSettingsEnabled = devSettingsEnabled,
                    ),
                ),
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            )

    fun setHasSyncEnabled(value: Boolean) {
        viewModelScope.launch {
            userDataRepository.setSyncDisabled(value)
        }
    }

    fun setDevSettingsEnabled(enabled: Boolean) {
        devSettingsEnabled = enabled
    }
}
