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
                    hasMetricsEnabled = true,
                    hasCrashlyticsEnabled = true,
                    shouldShowLoginScreenOnStartup = true,
                    isAppFirstLaunch = true,
                ),
                subject.userData.first(),
            )
        }
}
