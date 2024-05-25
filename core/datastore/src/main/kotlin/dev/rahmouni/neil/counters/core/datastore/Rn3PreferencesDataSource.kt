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

package dev.rahmouni.neil.counters.core.datastore

import androidx.datastore.core.DataStore
import dev.rahmouni.neil.counters.core.model.data.UserData
import kotlinx.coroutines.flow.map
import rahmouni.neil.counters.core.datastore.UserPreferences
import rahmouni.neil.counters.core.datastore.copy
import javax.inject.Inject

class Rn3PreferencesDataSource @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data.map {
        UserData(
            hasAccessibilityEmphasizedSwitchesEnabled = it.accessibilityHasEmphasizedSwitchesEnabled,
            hasAccessibilityIconTooltipsEnabled = !it.accessibilityHasIconTooltipsDisabled,
            hasMetricsEnabled = !it.hasMetricsDisabled,
            hasCrashlyticsEnabled = !it.hasCrashlyticsDisabled,
            hasSyncEnabled = it.hasSyncEnabled,
            lastUserUid = it.lastUserUid,
        )
    }

    suspend fun setAccessibilityEmphasizedSwitchesPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.accessibilityHasEmphasizedSwitchesEnabled = value }
        }
    }

    suspend fun setAccessibilityIconTooltipsPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.accessibilityHasIconTooltipsDisabled = !value }
        }
    }

    suspend fun setSyncEnabledPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.hasSyncEnabled = value }
        }
    }

    suspend fun setMetricsEnabledPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.hasMetricsDisabled = !value }
        }
    }

    suspend fun setCrashlyticsEnabledPreference(value: Boolean) {
        userPreferences.updateData {
            it.copy { this.hasCrashlyticsDisabled = !value }
        }
    }

    suspend fun setLastUserUidPreference(value: String) {
        userPreferences.updateData {
            it.copy { this.lastUserUid = value }
        }
    }
}
