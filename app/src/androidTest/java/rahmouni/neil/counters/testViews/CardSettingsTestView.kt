package rahmouni.neil.counters.testViews

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import rahmouni.neil.counters.MainActivity
import rahmouni.neil.counters.R

class CardSettingsTestView(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    private val counterName: String? = null
) {
    //----------- ACTIONS ----------//

    fun openSettingsTab(): SettingsTestView {
        composeTestRule
            .onNodeWithTag("BACK_ARROW")
            .performClick()

        return SettingsTestView(composeTestRule, counterName)
    }

    //---------- ASSERTS ----------//

    fun assertTitleExists(): CardSettingsTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.cardSettingsActivity_topbar_title))
            .assertExists()

        return this
    }
}