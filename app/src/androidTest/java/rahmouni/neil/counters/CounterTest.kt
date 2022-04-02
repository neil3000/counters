package rahmouni.neil.counters

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import rahmouni.neil.counters.testViews.HomeScreenTestView

class CounterTest {

    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test
    fun openApp() {
        HomeScreenTestView(rule)
            .assertTitleExists()
    }

    @Test
    fun createCounter() {
        HomeScreenTestView(rule)
            .openNewCounterModal()
            .setRandomName()
            .create()
            .assertCounterExists()
    }

    @Test
    fun openCounterSettings() {
        HomeScreenTestView(rule)
            .openNewCounterModal()
            .setRandomName()
            .create()
            .openCounter()
            .assertNoEntriesExist()
    }

    @Test
    fun changeResetTypeFromSettings() {
        val settings = HomeScreenTestView(rule)
            .openNewCounterModal()
            .setRandomName()
            .create()
            .openCounter()
            .openSettingsTab()

        for (resetType in ResetType.values()) {
            settings
                .setResetType(resetType)
                .assertResetTypeIs(resetType)
        }
    }

    @Test
    fun changeResetTypeFromModal() {
        for (resetType in ResetType.values()) {
            HomeScreenTestView(rule)
                .openNewCounterModal()
                .setResetType(resetType)
                .assertResetTypeIs(resetType)
        }
    }

    @Test
    fun resetTypeFromModalAffectsSettings() {
        for (resetType in ResetType.values()) {
            HomeScreenTestView(rule)
                .openNewCounterModal()
                .setRandomName()
                .setResetType(resetType)
                .create()
                .openCounter()
                .openSettingsTab()
                .assertResetTypeIs(resetType)
                .openHomeScreen()
        }
    }

    @Test
    fun cardIncrease() {
        HomeScreenTestView(rule)
            .openNewCounterModal()
            .setRandomName()
            .create()
            .openNewEntryModalOfCounterIncrease()
            .addEntry()
            .assertCounterValueIs(1)
    }

    @Test
    fun test() {
                HomeScreenTestView(rule)
                    .populateDatabaseWithResetEntries()
    }
}