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

package dev.rahmouni.neil.counters.core.data.repository

import dev.rahmouni.neil.counters.core.analytics.AnalyticsHelper
import dev.rahmouni.neil.counters.core.datastore.Rn3PreferencesDataSource
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

    override suspend fun setMetricsEnabled(value: Boolean) {
        rn3PreferencesDataSource.setMetricsEnabledPreference(value)
        analyticsHelper.logMetricsPreferenceChanged(value)
    }

    override suspend fun setCrashlyticsEnabled(value: Boolean) {
        rn3PreferencesDataSource.setCrashlyticsEnabledPreference(value)
        analyticsHelper.logCrashlyticsPreferenceChanged(value)
    }

    override suspend fun setSyncEnabled(value: Boolean) {
        rn3PreferencesDataSource.setSyncEnabledPreference(value)
        analyticsHelper.logSyncPreferenceChanged(value)
    }

    override suspend fun setLastUserUid(value: String) {
        rn3PreferencesDataSource.setLastUserUidPreference(value)
    }
}
