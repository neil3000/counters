package rahmouni.neil.counters

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import java.util.*

fun openNewCounterModal(composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    composeTestRule
        .onNodeWithText(composeTestRule.activity.getString(R.string.action_newCounter_short))
        .assertHasClickAction()
        .performClick()
}

fun setCounterNameFromModal(composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>, counterName: String) {
    // Put "Test name" in the Name field
    composeTestRule
        .onNodeWithText(composeTestRule.activity.getString(R.string.text_name))
        .assertExists()
        .performClick()
        .assertIsFocused()
        .performTextInput(counterName)
}

fun setRandomCounterNameFromModal(composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>): String {
    val counterName = "Test " + UUID.randomUUID().toString().substring(0, 8)

    setCounterNameFromModal(composeTestRule, counterName)

    return counterName
}

fun createCounterFromModal(composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>) {
    // Click on the "Create" button
    composeTestRule
        .onNodeWithText(composeTestRule.activity.getString(R.string.action_create_short))
        .assertHasClickAction()
        .performClick()
}

fun createTestCounter(composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>): String {
    openNewCounterModal(composeTestRule)
    val counterName = setRandomCounterNameFromModal(composeTestRule)
    createCounterFromModal(composeTestRule)
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
        .performClick()

    // Click on save button of dialog
    composeTestRule
        .onNodeWithText(composeTestRule.activity.getString(R.string.action_save_short))
        .assertHasClickAction()
        .performClick()
}

fun assertResetTypeFromSettingsIs(composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>, resetType: ResetType) {
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