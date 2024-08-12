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

package dev.rahmouni.neil.counters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.MainActivityUiState.Loading
import dev.rahmouni.neil.counters.MainActivityUiState.Success
import dev.rahmouni.neil.counters.core.accessibility.AccessibilityHelper
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.data.repository.userData.UserDataRepository
import dev.rahmouni.neil.counters.core.model.data.AddressInfo
import dev.rahmouni.neil.counters.core.user.Rn3User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import rahmouni.neil.counters.BuildConfig
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    authHelper: AuthHelper,
) : ViewModel() {

    private var isAppFirstLaunch = false
    private var shouldShowLoginScreenOnStartup = false
    private var needInformation = false

    @Suppress("KotlinConstantConditions")
    val uiState: StateFlow<MainActivityUiState> =
        authHelper.getUserFlow().combine(userDataRepository.userData) { user, userData ->
            if (user == Rn3User.Loading) {
                Loading
            } else {
                if (userData.isAppFirstLaunch) isAppFirstLaunch = true
                if (userData.shouldShowLoginScreenOnStartup) shouldShowLoginScreenOnStartup = true
                if (userData.needInformation) needInformation = true

                Success(
                    accessibilityHelper = AccessibilityHelper(
                        hasEmphasizedSwitchesEnabled = userData.hasAccessibilityEmphasizedSwitchesEnabled,
                        hasIconTooltipsEnabled = userData.hasAccessibilityIconTooltipsEnabled,
                        hasAltTextEnabled = userData.hasAccessibilityAltTextEnabled,
                    ),
                    hasMetricsEnabled = userData.hasMetricsEnabled,
                    hasCrashlyticsEnabled = userData.hasCrashlyticsEnabled,
                    hasTravelModeEnabled = userData.hasTravelModeEnabled,
                    hasFriendsMainEnabled = userData.hasFriendsMainEnabled,
                    needInformation = BuildConfig.FLAVOR != "demo" && needInformation,
                    isAppFirstLaunch = BuildConfig.FLAVOR != "demo" && isAppFirstLaunch,
                    shouldShowLoginScreenOnStartup = BuildConfig.FLAVOR != "demo" && shouldShowLoginScreenOnStartup,
                    address = userData.address,
                    phone = userData.phone,
                )
            }
        }.stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        )

    fun setNotAppFirstLaunch() {
        viewModelScope.launch {
            userDataRepository.setNotAppFirstLaunch()
        }
    }
}

sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
    data class Success(
        val accessibilityHelper: AccessibilityHelper,
        val hasMetricsEnabled: Boolean,
        val hasCrashlyticsEnabled: Boolean,
        val isAppFirstLaunch: Boolean,
        val shouldShowLoginScreenOnStartup: Boolean,
        val needInformation: Boolean,
        val hasTravelModeEnabled: Boolean,
        val hasFriendsMainEnabled: Boolean,
        val address: AddressInfo,
        val phone: PhoneNumber?,
    ) : MainActivityUiState
}
