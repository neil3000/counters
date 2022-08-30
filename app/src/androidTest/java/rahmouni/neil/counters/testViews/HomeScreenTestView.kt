package rahmouni.neil.counters.testViews

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import rahmouni.neil.counters.*
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
            .onNodeWithText(composeTestRule.activity.getString(R.string.mainActivity_fab_newCounter))
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
            .onNodeWithText(name ?: counterName!!)
            .performTouchInput { longClick() }

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

    fun createUSScreenshotCounters(): HomeScreenTestView {
        val pushupsUid: Long =
            CountersApplication.instance.countersListRepository.testAddCounter(Counter(displayName = "Push-ups", resetType = ResetType.DAY, style = CounterStyle.SECONDARY))
        val waterUid: Long =
            CountersApplication.instance.countersListRepository.testAddCounter(Counter(displayName = "Water drinks", style = CounterStyle.PRIMARY))
        val medicationUid: Long =
            CountersApplication.instance.countersListRepository.testAddCounter(Counter(displayName = "Medication"))
        val weightUid: Long =
            CountersApplication.instance.countersListRepository.testAddCounter(Counter(displayName = "Weight (kg)"))
        val lapMinutesUid: Long =
            CountersApplication.instance.countersListRepository.testAddCounter(Counter(displayName = "Lap minutes", style = CounterStyle.TERTIARY))
        for (i in 0..8) {
            CountersApplication.instance.countersListRepository.testAddIncrement(
                Increment(
                    counterID = pushupsUid.toInt(),
                    value = listOf(30, 30, 25, 30, 35, 35, 30, 30, 25)[i],
                    timestamp = LocalDateTime.now().minusDays(listOf(0,0,1,1,1,2,2,2,3)[i].toLong())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd "))+listOf("07:32:30","08:01:30","07:24:30","16:58:30","22:24:30","07:16:30","17:13:30","22:09:30","22:18:30")[i]
                )
            )
        }
        CountersApplication.instance.countersListRepository.testAddIncrement(
            Increment(
                counterID = waterUid.toInt(),
                value = 3,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        )
        CountersApplication.instance.countersListRepository.testAddIncrement(
            Increment(
                counterID = medicationUid.toInt(),
                value = 1,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        )
        CountersApplication.instance.countersListRepository.testAddIncrement(
            Increment(
                counterID = weightUid.toInt(),
                value = 62,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        )
        CountersApplication.instance.countersListRepository.testAddIncrement(
            Increment(
                counterID = lapMinutesUid.toInt(),
                value = 12,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        )

        return this
    }

    fun createFRScreenshotCounters(): HomeScreenTestView {
        val pushupsUid: Long =
            CountersApplication.instance.countersListRepository.testAddCounter(Counter(displayName = "Pompes", resetType = ResetType.DAY, style = CounterStyle.SECONDARY))
        val waterUid: Long =
            CountersApplication.instance.countersListRepository.testAddCounter(Counter(displayName = "Verres d'eau", style = CounterStyle.PRIMARY))
        val medicationUid: Long =
            CountersApplication.instance.countersListRepository.testAddCounter(Counter(displayName = "Médicaments"))
        val weightUid: Long =
            CountersApplication.instance.countersListRepository.testAddCounter(Counter(displayName = "Poids (kg)"))
        val lapMinutesUid: Long =
            CountersApplication.instance.countersListRepository.testAddCounter(Counter(displayName = "Minutes par tour", style = CounterStyle.TERTIARY))
        for (i in 0..8) {
            CountersApplication.instance.countersListRepository.testAddIncrement(
                Increment(
                    counterID = pushupsUid.toInt(),
                    value = listOf(30, 30, 25, 30, 35, 35, 30, 30, 25)[i],
                    timestamp = LocalDateTime.now().minusDays(listOf(0,0,1,1,1,2,2,2,3)[i].toLong())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd "))+listOf("07:32:30","08:01:30","07:24:30","16:58:30","22:24:30","07:16:30","17:13:30","22:09:30","22:18:30")[i]
                )
            )
        }
        CountersApplication.instance.countersListRepository.testAddIncrement(
            Increment(
                counterID = waterUid.toInt(),
                value = 3,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        )
        CountersApplication.instance.countersListRepository.testAddIncrement(
            Increment(
                counterID = medicationUid.toInt(),
                value = 1,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        )
        CountersApplication.instance.countersListRepository.testAddIncrement(
            Increment(
                counterID = weightUid.toInt(),
                value = 62,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        )
        CountersApplication.instance.countersListRepository.testAddIncrement(
            Increment(
                counterID = lapMinutesUid.toInt(),
                value = 12,
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
            )
        )

        return this
    }
}