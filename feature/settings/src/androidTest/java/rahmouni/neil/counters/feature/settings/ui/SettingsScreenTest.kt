package rahmouni.neil.counters.feature.settings.ui

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import dev.rahmouni.neil.counters.feature.settings.main.SettingsScreen

/**
 * UI tests for [SettingsScreen].
 */
@RunWith(AndroidJUnit4::class)
class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Before
    fun setup() {
        composeTestRule.setContent {
            SettingsScreen(onBackIconButtonClicked = {}, onClickDataAndPrivacyTile = {}, onClickAccessibilityTile = {})
        }
    }
    @Test
    fun pageTitleIsVisible() {
        composeTestRule.onNodeWithText("Settings").assertExists()
    }
}