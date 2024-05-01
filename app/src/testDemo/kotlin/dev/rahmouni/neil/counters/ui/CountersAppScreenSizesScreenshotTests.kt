package dev.rahmouni.neil.counters.ui

import android.util.Log
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.platform.app.InstrumentationRegistry
import androidx.work.Configuration
import androidx.work.testing.SynchronousExecutor
import androidx.work.testing.WorkManagerTestInitHelper
import com.github.takahirom.roborazzi.captureRoboImage
import dagger.hilt.android.testing.BindValue
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import dev.rahmouni.neil.counters.core.data.repository.UserDataRepository
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.testing.util.DefaultRoborazziOptions
import dev.rahmouni.neil.counters.uitesthiltmanifest.HiltComponentActivity
import kotlinx.coroutines.runBlocking
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
import javax.inject.Inject

/**
 * Tests that the navigation UI is rendered correctly on different screen sizes.
 */
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
// Configure Robolectric to use a very large screen size that can fit all of the test sizes.
// This allows enough room to render the content under test without clipping or scaling.
@Config(application = HiltTestApplication::class, qualifiers = "w1000dp-h1000dp-480dpi")
@LooperMode(LooperMode.Mode.PAUSED)
@HiltAndroidTest
class Rn3AppScreenSizesScreenshotTests {

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

    @Inject
    lateinit var userDataRepository: UserDataRepository

    @Before
    fun setup() {
        val config = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setExecutor(SynchronousExecutor())
            .build()

        // Initialize WorkManager for instrumentation tests.
        WorkManagerTestInitHelper.initializeTestWorkManager(
            InstrumentationRegistry.getInstrumentation().context,
            config,
        )

        hiltRule.inject()

        // Configure user data
        runBlocking {
            //nothing much to do yet
        }
    }

    @Before
    fun setTimeZone() {
        // Make time zone deterministic in tests
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    private fun testRn3AppScreenshotWithSize(width: Dp, height: Dp, screenshotName: String) {
        composeTestRule.setContent {
            CompositionLocalProvider(
                LocalInspectionMode provides true,
            ) {
                DeviceConfigurationOverride(
                    override = DeviceConfigurationOverride.ForcedSize(DpSize(width, height)),
                ) {
                    Rn3Theme {
                        val fakeAppState = rememberCountersAppState(
                            windowSizeClass = WindowSizeClass.calculateFromSize(
                                DpSize(width, height),
                            ),
                        )
                        CountersApp(fakeAppState)
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
