package rahmouni.neil.counters

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import org.junit.Rule
import org.junit.Test
import java.util.*

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
    fun createDefaultCounter() {
        val counterName = "Test " + UUID.randomUUID().toString().substring(0,10)

        // Click on the fab
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.action_newCounter_short))
            .assertHasClickAction()
            .performClick()

        // Put "Test name" in the Name field
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_name))
            .assertExists()
            .performClick()
            .assertIsFocused()
            .performTextInput(counterName)

        // Click on the "Create" button
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.action_create_short))
            .assertHasClickAction()
            .performClick()

        // Verify if counter created with default value 0
        composeTestRule
            .onNodeWithText(counterName)
            .assertHasClickAction()
            .assertTextContains("0")
    }
}