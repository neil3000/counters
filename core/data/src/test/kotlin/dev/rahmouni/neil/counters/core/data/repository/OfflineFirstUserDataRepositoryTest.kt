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

package dev.rahmouni.neil.counters.core.data.repository

import dev.rahmouni.neil.counters.core.analytics.NoOpAnalyticsHelper
import dev.rahmouni.neil.counters.core.data.repository.userData.OfflineFirstUserDataRepository
import dev.rahmouni.neil.counters.core.datastore.Rn3PreferencesDataSource
import dev.rahmouni.neil.counters.core.datastore.test.testUserPreferencesDataStore
import dev.rahmouni.neil.counters.core.model.data.UserData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertEquals

class OfflineFirstUserDataRepositoryTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: OfflineFirstUserDataRepository

    private lateinit var rn3PreferencesDataSource: Rn3PreferencesDataSource

    private val analyticsHelper = NoOpAnalyticsHelper()

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        rn3PreferencesDataSource = Rn3PreferencesDataSource(
            tmpFolder.testUserPreferencesDataStore(testScope),
        )

        subject = OfflineFirstUserDataRepository(
            rn3PreferencesDataSource = rn3PreferencesDataSource,
            analyticsHelper,
        )
    }

    @Test
    fun offlineFirstUserDataRepository_default_user_data_is_correct() =
        testScope.runTest {
            assertEquals(
                UserData(
                    hasAccessibilityEmphasizedSwitchesEnabled = false,
                    hasAccessibilityIconTooltipsEnabled = true,
                    hasAccessibilityAltTextEnabled = false,
                    hasTravelModeEnabled = false,
                    hasFriendsMainEnabled = false,
                    hasMetricsEnabled = true,
                    hasCrashlyticsEnabled = true,
                    shouldShowLoginScreenOnStartup = true,
                    isAppFirstLaunch = true,
                ),
                subject.userData.first(),
            )
        }
}
