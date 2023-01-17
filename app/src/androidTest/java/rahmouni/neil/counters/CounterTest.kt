package rahmouni.neil.counters

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import rahmouni.neil.counters.testViews.HomeScreenTestView


class CounterTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun openApp() {
        HomeScreenTestView(rule)
            .assertTitleExists()
    }

    @Test
    fun openSettings() {
        HomeScreenTestView(rule)
            .openSettings()
            .assertTitleExists()

    }

    @Test
    fun openDataSettings() {
        HomeScreenTestView(rule)
            .openSettings()
            .openDataSettingsScreen()
            .assertTitleExists()
    }

    @Test
    fun test() {
        HomeScreenTestView(rule)
            .createUSScreenshotCounters()
    }
}