package rahmouni.neil.counters.testViews

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import rahmouni.neil.counters.MainActivity
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType
import java.util.*

class NewCounterModalTestView(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    private var counterName: String? = null
) {
    //----------- ACTIONS ----------//

    fun setName(name: String): NewCounterModalTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_name))
            .performTextInput(name)

        counterName = name
        return this
    }

    fun setRandomName(): NewCounterModalTestView {
        setName("Test " + UUID.randomUUID().toString().substring(0, 8))

        return this
    }

    fun create(): HomeScreenTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.action_create_short))
            .performClick()

        return HomeScreenTestView(composeTestRule, counterName)
    }

    fun setResetType(resetType: ResetType): NewCounterModalTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_reset))
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(resetType.title))
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.action_save_short))
            .performClick()

        return this
    }

    //---------- ASSERTS ----------//

    fun assertResetTypeIs(resetType: ResetType): NewCounterModalTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(resetType.formatted, 0))
            .assertExists()

        return this
    }
}