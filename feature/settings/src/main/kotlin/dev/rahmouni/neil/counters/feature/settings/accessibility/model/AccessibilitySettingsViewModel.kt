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

package dev.rahmouni.neil.counters.feature.settings.accessibility.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.data.repository.userData.UserDataRepository
import dev.rahmouni.neil.counters.feature.settings.accessibility.model.data.AccessibilitySettingsData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AccessibilitySettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
) : ViewModel() {

    val uiState: StateFlow<AccessibilitySettingsUiState> =
        userDataRepository.userData
            .map { userData ->
                AccessibilitySettingsUiState.Success(
                    accessibilitySettingsData = AccessibilitySettingsData(
                        hasEmphasizedSwitchesEnabled = userData.hasAccessibilityEmphasizedSwitchesEnabled,
                        hasIconTooltipsEnabled = userData.hasAccessibilityIconTooltipsEnabled,
                    ),
                )
            }.stateIn(
                scope = viewModelScope,
                initialValue = AccessibilitySettingsUiState.Loading,
                started = WhileSubscribed(5.seconds.inWholeMilliseconds),
            )

    fun setEmphasizedSwitches(value: Boolean) {
        viewModelScope.launch {
            userDataRepository.setAccessibilityEmphasizedSwitches(value)
        }
    }

    fun setIconTooltips(value: Boolean) {
        viewModelScope.launch {
            userDataRepository.setAccessibilityIconTooltips(value)
        }
    }
}
