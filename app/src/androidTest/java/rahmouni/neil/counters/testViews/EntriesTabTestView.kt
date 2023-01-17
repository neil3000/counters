package rahmouni.neil.counters.testViews

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import rahmouni.neil.counters.MainActivity
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.counter_card.activity.GroupType

class EntriesTabTestView(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    private val counterName: String? = null
) {
    //----------- ACTIONS ----------//

    /*fun openSettingsTab(): SettingsTabTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_counterSettings_short))
            .performClick()

        return SettingsTabTestView(composeTestRule, counterName)
    }*/

    fun setGroupTypeOfResetType(rt: ResetType): EntriesTabTestView {
        for (gt in GroupType.values()) {
            if (gt.resetType == rt) {
                composeTestRule
                    .onNodeWithText(composeTestRule.activity.getString(gt.title))
                    .performClick()
            }
        }

        return this
    }

    //---------- ASSERTS ----------//

    fun assertFirstHeaderTitleIs(title: Int): EntriesTabTestView {
        composeTestRule
            .onAllNodesWithTag("HEADER_TITLE")
            .onFirst()
            .assertTextContains(composeTestRule.activity.getString(title))

        return this
    }

    fun assertNoHeaderExists(): EntriesTabTestView {
        composeTestRule
            .onNodeWithTag("HEADER_TITLE")
            .assertDoesNotExist()

        return this
    }

    fun assertResetTypeGroupSelectedIs(rt: ResetType): EntriesTabTestView {
        for (gt in GroupType.values()) {
            if (gt.resetType == rt) {
                composeTestRule
                    .onNodeWithText(composeTestRule.activity.getString(gt.title))
                    .assertIsSelected()
            } else {
                composeTestRule
                    .onNodeWithText(composeTestRule.activity.getString(gt.title))
                    .assertIsNotSelected()
            }
        }

        return this
    }
}