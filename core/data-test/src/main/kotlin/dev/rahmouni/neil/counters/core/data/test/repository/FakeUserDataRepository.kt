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

package dev.rahmouni.neil.counters.core.data.test.repository

import dev.rahmouni.neil.counters.core.data.repository.UserDataRepository
import dev.rahmouni.neil.counters.core.datastore.Rn3PreferencesDataSource
import dev.rahmouni.neil.counters.core.model.data.UserData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Fake implementation of the [UserDataRepository] that returns hardcoded user data.
 *
 * This allows us to run the app with fake data, without needing an internet connection or working
 * backend.
 */
class FakeUserDataRepository @Inject constructor(
    private val rn3PreferencesDataSource: Rn3PreferencesDataSource,
) : UserDataRepository {

    override val userData: Flow<UserData> =
        rn3PreferencesDataSource.userData

    override suspend fun setAccessibilityEmphasizedSwitches(value: Boolean) {
        rn3PreferencesDataSource.setAccessibilityEmphasizedSwitchesPreference(value)
    }

    override suspend fun setAccessibilityIconTooltips(value: Boolean) {
        rn3PreferencesDataSource.setAccessibilityIconTooltipsPreference(value)
    }

    override suspend fun setMetricsEnabled(value: Boolean) {
        rn3PreferencesDataSource.setMetricsEnabledPreference(value)
    }

    override suspend fun setCrashlyticsEnabled(value: Boolean) {
        rn3PreferencesDataSource.setCrashlyticsEnabledPreference(value)
    }

    override suspend fun setLastUserUid(value: String) {
        rn3PreferencesDataSource.setLastUserUidPreference(value)
    }
}
