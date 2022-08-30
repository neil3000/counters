package rahmouni.neil.counters.testViews

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import rahmouni.neil.counters.MainActivity
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType

class SettingsTabTestView(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    private val counterName: String? = null
) {
    //----------- ACTIONS ----------//

    fun openEntriesTab(): EntriesTabTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_entries_short))
            .performClick()

        return EntriesTabTestView(composeTestRule, counterName)
    }

    fun openCardSettings(): CardSettingsTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.cardSettingsActivity_topbar_title))
            .performClick()

        return CardSettingsTestView(composeTestRule, counterName)
    }

    fun setResetType(resetType: ResetType): SettingsTabTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_frequency))
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(resetType.title()))
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.action_save_short))
            .performClick()

        return this
    }

    fun openHomeScreen(): HomeScreenTestView {
        composeTestRule
            .onNodeWithTag("BACK_ARROW")
            .performClick()

        return HomeScreenTestView(composeTestRule, counterName)
    }

    //---------- ASSERTS ----------//

    fun assertResetTypeIs(resetType: ResetType): SettingsTabTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(resetType.formatted(), 0))
            .assertExists()

        return this
    }
}