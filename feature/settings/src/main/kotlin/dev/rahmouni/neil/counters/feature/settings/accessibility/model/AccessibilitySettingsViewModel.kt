/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
