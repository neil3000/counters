/*
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.MainActivityUiState.Loading
import dev.rahmouni.neil.counters.MainActivityUiState.Success
import dev.rahmouni.neil.counters.core.accessibility.AccessibilityHelper
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.data.repository.userData.UserDataRepository
import dev.rahmouni.neil.counters.core.model.data.AddressInfo
import dev.rahmouni.neil.counters.core.model.data.PhoneInfo
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
        val phone: PhoneInfo,
    ) : MainActivityUiState
}
