package rahmouni.neil.counters

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import java.util.*

fun createTestCounter(composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>): String {
    val counterName = "Test " + UUID.randomUUID().toString().substring(0, 8)

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

    return counterName
}

fun navigateToCounterSettings(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    counterName: String
) {
    composeTestRule
        .onNodeWithText(counterName)
        .assertHasClickAction()
        .performClick()
    composeTestRule
        .onNodeWithText(composeTestRule.activity.getString(R.string.text_counterSettings_short))
        .assertHasClickAction()
        .performClick()
}

fun changeResetTypeTo(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    resetType: ResetType
) {
    // Click on the reset option
    composeTestRule
        .onNodeWithText(composeTestRule.activity.getString(R.string.text_reset))
        .assertHasClickAction()
        .performClick()

    // Click on the right ResetType
    composeTestRule
        .onNodeWithText(composeTestRule.activity.getString(resetType.title))
        .assertHasClickAction()
        .performClick()

    // Click on save button of dialog
    composeTestRule
        .onNodeWithText(composeTestRule.activity.getString(R.string.action_save_short))
        .assertHasClickAction()
        .performClick()

    // Check if the reset field has changed
    composeTestRule
        .onNodeWithText(composeTestRule.activity.getString(resetType.formatted, 0))
        .assertExists()
}

fun navigateToHomeScreen(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
) {
    // Click on back arrow
    composeTestRule
        .onNodeWithTag("BACK_ARROW")
        .assertHasClickAction()
        .performClick()

    // Check if on home screen
    composeTestRule
        .onNodeWithText(composeTestRule.activity.getString(R.string.text_appName))
        .assertExists()
}

fun counterCardIncrease(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    counterName: String
) {
    // Click on plus button
    composeTestRule
        .onNodeWithTag(counterName+"_CARD_INCREASE")
        .assertHasClickAction()
        .performClick()

    // Click on add entry
    composeTestRule
        .onNodeWithText(composeTestRule.activity.getString(R.string.action_addEntry))
        .assertHasClickAction()
        .performClick()
}

fun assertCounterCardCount(
    composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    counterName: String,
    value: Int
) {
    composeTestRule
        .onNodeWithText(counterName)
        .assertTextContains(value.toString())
}