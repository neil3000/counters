package rahmouni.neil.counters.testViews

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import rahmouni.neil.counters.MainActivity
import rahmouni.neil.counters.R

class SettingsTestView(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    private var counterName: String? = null
) {
    //----------- ACTIONS ----------//

    fun openHomeScreen(): HomeScreenTestView {
        composeTestRule
            .onNodeWithTag("BACK_ARROW")
            .performClick()

        return HomeScreenTestView(composeTestRule, counterName)
    }

    fun openDataSettingsScreen(): DataSettingsTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.settingsActivity_tile_dataAndPrivacy_title))
            .performClick()

        return DataSettingsTestView(composeTestRule, counterName)
    }

    //---------- ASSERTS ----------//

    fun assertTitleExists(): SettingsTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.settingsActivity_topbar_title))
            .assertExists()

        return this
    }
}