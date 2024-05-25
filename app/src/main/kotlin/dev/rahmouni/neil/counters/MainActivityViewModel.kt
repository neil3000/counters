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

package dev.rahmouni.neil.counters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.MainActivityUiState.Loading
import dev.rahmouni.neil.counters.MainActivityUiState.Success
import dev.rahmouni.neil.counters.core.accessibility.AccessibilityHelper
import dev.rahmouni.neil.counters.core.data.repository.UserDataRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    userDataRepository: UserDataRepository,
) : ViewModel() {
    val uiState: StateFlow<MainActivityUiState> = userDataRepository.userData.map { userData ->
        Success(
            accessibilityHelper = AccessibilityHelper(
                hasEmphasizedSwitchesEnabled = userData.hasAccessibilityEmphasizedSwitchesEnabled,
                hasIconTooltipsEnabled = userData.hasAccessibilityIconTooltipsEnabled,
            ),
            hasSyncEnabled = userData.hasSyncEnabled
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = Loading,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
    )
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(val accessibilityHelper: AccessibilityHelper, val hasSyncEnabled: Boolean) :
        MainActivityUiState
}
