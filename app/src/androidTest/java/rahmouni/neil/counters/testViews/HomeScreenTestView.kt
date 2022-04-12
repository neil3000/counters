package rahmouni.neil.counters.testViews

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.MainActivity
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.Counter
import rahmouni.neil.counters.database.Increment
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.*

class HomeScreenTestView(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>,
    private var counterName: String? = null
) {
    //----------- ACTIONS ----------//

    fun openNewCounterModal(): NewCounterModalTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.action_newCounter_short))
            .performClick()

        return NewCounterModalTestView(composeTestRule, counterName)
    }

    fun openCounter(name: String? = null): EntriesTabTestView {
        if (name == null && counterName == null) throwCounterInitializationException()

        composeTestRule
            .onNodeWithText(name ?: counterName!!)
            .performClick()

        return EntriesTabTestView(composeTestRule, counterName)
    }

    fun openNewEntryModalOfCounterIncrease(name: String? = null): HomeScreenNewEntryModalTestView {
        if (name == null && counterName == null) throwCounterInitializationException()

        composeTestRule
            .onNodeWithTag(counterName + "_CARD_INCREASE")
            .performClick()

        return HomeScreenNewEntryModalTestView(composeTestRule, counterName)
    }

    fun openSettings(): SettingsTestView {
        composeTestRule
            .onNodeWithTag("SETTINGS_DOTS")
            .performClick()
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_settings))
            .performClick()

        return SettingsTestView(composeTestRule, counterName)
    }

    //---------- ASSERTS ----------//

    fun assertTitleExists(): HomeScreenTestView {
        composeTestRule
            .onNodeWithText(composeTestRule.activity.getString(R.string.text_appName))
            .assertExists()

        return this
    }

    fun assertCounterExists(name: String? = null): HomeScreenTestView {
        if (name == null && counterName == null) throwCounterInitializationException()

        composeTestRule
            .onNodeWithText(name ?: counterName!!)
            .assertExists()

        return this
    }

    fun assertCounterValueIs(value: Int, name: String? = null): HomeScreenTestView {
        if (name == null && counterName == null) throwCounterInitializationException()

        composeTestRule
            .onNodeWithText(name ?: counterName!!)
            .assertTextContains(value.toString())

        return this
    }

    //----------- UTILS -----------//

    private fun throwCounterInitializationException() {
        throw Exception("Test counter not created & counter name not specified. One of those has to exist for this to work.")
    }

    fun createEntriesTestCounter(): HomeScreenTestView {
        counterName = "Test " + UUID.randomUUID().toString().substring(0, 8)

        val uid: Long =
            CountersApplication.instance.countersListRepository.testAddCounter(Counter(displayName = counterName!!))
        for (i in 0..59) {
            CountersApplication.instance.countersListRepository.testAddIncrement(
                Increment(
                    counterID = uid.toInt(),
                    value = i + 1,
                    timestamp = LocalDateTime.of(2022, Month.APRIL, 3, 18, 48).minusDays(i.toLong())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                )
            )
        }

        return this
    }
}