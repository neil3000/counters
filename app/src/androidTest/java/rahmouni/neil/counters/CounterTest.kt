package rahmouni.neil.counters

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class CounterTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun openAppTest() {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_appName))
            .assertExists()
    }

    @Test
    fun createCounter() {
        createTestCounter(composeTestRule)
    }

    @Test
    fun openCounterSettings() {
        val counterName = createTestCounter(composeTestRule)
        navigateToCounterSettings(composeTestRule, counterName)
    }

    @Test
    fun changeResetTypeFromSettings() {
        val counterName = createTestCounter(composeTestRule)
        navigateToCounterSettings(composeTestRule, counterName)

        for (resetType in ResetType.values()) {
            changeResetTypeTo(composeTestRule, resetType)
        }
    }
}