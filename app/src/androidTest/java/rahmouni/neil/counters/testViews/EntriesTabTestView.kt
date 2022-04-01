package rahmouni.neil.counters.testViews

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import rahmouni.neil.counters.MainActivity
import rahmouni.neil.counters.R

class EntriesTabTestView(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    private val counterName: String? = null
) {
    //----------- ACTIONS ----------//

    fun openSettingsTab(): SettingsTabTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_counterSettings_short))
            .performClick()

        return SettingsTabTestView(composeTestRule, counterName)
    }

    //---------- ASSERTS ----------//

    fun assertNoEntriesExist(): EntriesTabTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_noEntriesYet))
            .assertExists()

        return this
    }
}