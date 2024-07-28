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

package dev.rahmouni.neil.counters.core.data.repository.userData

import dev.rahmouni.neil.counters.core.analytics.AnalyticsHelper
import dev.rahmouni.neil.counters.core.data.repository.logAccessibilityAltTextPreferenceChanged
import dev.rahmouni.neil.counters.core.data.repository.logAccessibilityEmphasizedSwitchesPreferenceChanged
import dev.rahmouni.neil.counters.core.data.repository.logAccessibilityIconTooltipsPreferenceChanged
import dev.rahmouni.neil.counters.core.data.repository.logCrashlyticsPreferenceChanged
import dev.rahmouni.neil.counters.core.data.repository.logFriendsMainPreferenceChanged
import dev.rahmouni.neil.counters.core.data.repository.logMetricsPreferenceChanged
import dev.rahmouni.neil.counters.core.data.repository.logTravelModePreferenceChanged
import dev.rahmouni.neil.counters.core.datastore.Rn3PreferencesDataSource
import dev.rahmouni.neil.counters.core.model.data.AddressInfo
import dev.rahmouni.neil.counters.core.model.data.PhoneInfo
import dev.rahmouni.neil.counters.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class OfflineFirstUserDataRepository @Inject constructor(
    private val rn3PreferencesDataSource: Rn3PreferencesDataSource,
    private val analyticsHelper: AnalyticsHelper,
) : UserDataRepository {

    override val userData: Flow<UserData> = rn3PreferencesDataSource.userData

    override suspend fun setAccessibilityEmphasizedSwitches(value: Boolean) {
        rn3PreferencesDataSource.setAccessibilityEmphasizedSwitchesPreference(value)
        analyticsHelper.logAccessibilityEmphasizedSwitchesPreferenceChanged(value)
    }

    override suspend fun setAccessibilityIconTooltips(value: Boolean) {
        rn3PreferencesDataSource.setAccessibilityIconTooltipsPreference(value)
        analyticsHelper.logAccessibilityIconTooltipsPreferenceChanged(value)
    }

    override suspend fun setAccessibilityAltText(value: Boolean) {
        rn3PreferencesDataSource.setAccessibilityAltTextPreference(value)
        analyticsHelper.logAccessibilityAltTextPreferenceChanged(value)
    }

    override suspend fun setMetricsEnabled(value: Boolean) {
        rn3PreferencesDataSource.setMetricsEnabledPreference(value)
        analyticsHelper.logMetricsPreferenceChanged(value)
    }

    override suspend fun setCrashlyticsEnabled(value: Boolean) {
        rn3PreferencesDataSource.setCrashlyticsEnabledPreference(value)
        analyticsHelper.logCrashlyticsPreferenceChanged(value)
    }

    override suspend fun setTravelMode(value: Boolean) {
        rn3PreferencesDataSource.setTravelModeEnabledPreference(value)
        analyticsHelper.logTravelModePreferenceChanged(value)
    }

    override suspend fun setFriendsMain(value: Boolean) {
        rn3PreferencesDataSource.setFriendsMainEnabledPreference(value)
        analyticsHelper.logFriendsMainPreferenceChanged(value)
    }

    override suspend fun setNeedInformation(value: Boolean) {
        rn3PreferencesDataSource.setNeedInformation(value)
    }

    override suspend fun setShouldShowLoginScreenOnStartup(value: Boolean) =
        rn3PreferencesDataSource.setShouldShowLoginScreenOnStartup(value)

    override suspend fun setNotAppFirstLaunch() = rn3PreferencesDataSource.setNotAppFirstLaunch()

    override suspend fun setAddressInfo(value: AddressInfo) {
        rn3PreferencesDataSource.setAddressInfo(value)
    }

    override suspend fun setPhoneInfo(value: PhoneInfo) {
        rn3PreferencesDataSource.setPhoneInfo(value)
    }
}
