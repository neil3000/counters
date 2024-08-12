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

package dev.rahmouni.neil.counters.ui

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.testing.util.DefaultRoborazziOptions
import dev.rahmouni.neil.counters.feature.feed.publics.PUBLICFEED_ROUTE
import dev.rahmouni.neil.counters.uitesthiltmanifest.HiltComponentActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.annotation.LooperMode
import java.util.TimeZone

/**
 * Tests that the navigation UI is rendered correctly on different screen sizes.
 */
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
// Configure Robolectric to use a very large screen size that can fit all of the test sizes.
// This allows enough room to render the content under test without clipping or scaling.
@Config(application = HiltTestApplication::class, qualifiers = "w1000dp-h1000dp-480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
@HiltAndroidTest
class AppScreenSizesScreenshotTests {

    /**
     * Manages the components' state and is used to perform injection on your test
     */
    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    /**
     * Create a temporary folder used to create a Data Store file. This guarantees that
     * the file is removed in between each test, preventing a crash.
     */
    @BindValue
    @get:Rule(order = 1)
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    /**
     * Use a test activity to set the content on.
     */
    @get:Rule(order = 2)
    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Before
    fun setTimeZone() {
        // Make time zone deterministic in tests
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    private fun testRn3AppScreenshotWithSize(width: Dp, height: Dp, screenshotName: String) {
        composeTestRule.setContent {
            CompositionLocalProvider(
                value = LocalInspectionMode provides true,
            ) {
                DeviceConfigurationOverride(
                    override = DeviceConfigurationOverride.ForcedSize(DpSize(width, height)),
                ) {
                    Rn3Theme {
                        val fakeAppState = rememberAppState()
                        App(
                            appState = fakeAppState,
                            routes = listOf(PUBLICFEED_ROUTE),
                        )
                    }
                }
            }
        }

        composeTestRule.onRoot()
            .captureRoboImage(
                "src/testDemo/screenshots/$screenshotName.png",
                roborazziOptions = DefaultRoborazziOptions,
            )
    }

    @Test
    fun compactWidth_compactHeight() {
        testRn3AppScreenshotWithSize(
            400.dp,
            400.dp,
            "compactWidth_compactHeight",
        )
    }

    @Test
    fun mediumWidth_compactHeight() {
        testRn3AppScreenshotWithSize(
            610.dp,
            400.dp,
            "mediumWidth_compactHeight",
        )
    }

    @Test
    fun expandedWidth_compactHeight() {
        testRn3AppScreenshotWithSize(
            900.dp,
            400.dp,
            "expandedWidth_compactHeight",
        )
    }

    @Test
    fun compactWidth_mediumHeight() {
        testRn3AppScreenshotWithSize(
            400.dp,
            500.dp,
            "compactWidth_mediumHeight",
        )
    }

    @Test
    fun mediumWidth_mediumHeight() {
        testRn3AppScreenshotWithSize(
            610.dp,
            500.dp,
            "mediumWidth_mediumHeight",
        )
    }

    @Test
    fun expandedWidth_mediumHeight() {
        testRn3AppScreenshotWithSize(
            900.dp,
            500.dp,
            "expandedWidth_mediumHeight",
        )
    }

    @Test
    fun compactWidth_expandedHeight() {
        testRn3AppScreenshotWithSize(
            400.dp,
            1000.dp,
            "compactWidth_expandedHeight",
        )
    }

    @Test
    fun mediumWidth_expandedHeight() {
        testRn3AppScreenshotWithSize(
            610.dp,
            1000.dp,
            "mediumWidth_expandedHeight",
        )
    }

    @Test
    fun expandedWidth_expandedHeight() {
        testRn3AppScreenshotWithSize(
            900.dp,
            1000.dp,
            "expandedWidth_expandedHeight",
        )
    }
}
