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
        val counterName = createTestCounter(composeTestRule)
        assertCounterCardCount(composeTestRule, counterName, 0)
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
            assertResetTypeFromSettingsIs(composeTestRule, resetType)
        }
    }

    @Test
    fun changeResetTypeFromModal() {
        for (resetType in ResetType.values()) {
            openNewCounterModal(composeTestRule)
            val counterName = setRandomCounterNameFromModal(composeTestRule)
            changeResetTypeTo(composeTestRule, resetType)
            assertResetTypeFromSettingsIs(composeTestRule, resetType)
            createCounterFromModal(composeTestRule)
            navigateToCounterSettings(composeTestRule, counterName)
            assertResetTypeFromSettingsIs(composeTestRule, resetType)
            navigateToHomeScreen(composeTestRule)
        }
    }

    @Test
    fun cardIncrease() {
        val counterName = createTestCounter(composeTestRule)
        counterCardIncrease(composeTestRule, counterName)
        assertCounterCardCount(composeTestRule, counterName, 1)
    }

    // Don't know how to change system date, or "wait" without pausing real device
    /*@Test
    fun waitForReset() {
        val counterName = createTestCounter(composeTestRule)
        navigateToCounterSettings(composeTestRule, counterName)

        /*for (resetType in ResetType.values()) {
            changeResetTypeTo(composeTestRule, resetType)
        }*/
        changeResetTypeTo(composeTestRule, ResetType.DAY)
        navigateToHomeScreen(composeTestRule)
        counterCardIncrease(composeTestRule, counterName)
        Thread.sleep(ResetType.DAY.testMillis)

        await().atLeast(ResetType.DAY.testMillis, TimeUnit.MILLISECONDS)

        assertCounterCardCount(composeTestRule, counterName, 0)
    }*/
}