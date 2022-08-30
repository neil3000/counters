package rahmouni.neil.counters

import androidx.compose.ui.test.junit4.createAndroidComposeRule
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
    fun resetTypesChangeDefaultGroup() {
        val homeScreen = HomeScreenTestView(rule)
            .openNewCounterModal()
            .setRandomName()
            .create()
            .openNewEntryModalOfCounterIncrease()
            .addEntry()
            .openCounter()

        for (rt in ResetType.values()) {
            homeScreen
                .openSettingsTab()
                .setResetType(rt)
                .openHomeScreen()
                .openCounter()
                .assertResetTypeGroupSelectedIs(rt)
        }
    }

    @Test
    fun resetTypesCurrentGroupHeaderTitle() {
        val homeScreen = HomeScreenTestView(rule)
            .openNewCounterModal()
            .setRandomName()
            .create()
            .openNewEntryModalOfCounterIncrease()
            .addEntry()
            .openCounter()

        for (rt in ResetType.values()) {
            homeScreen
                .openSettingsTab()
                .setResetType(rt)
                .openHomeScreen()
                .openCounter()

            if (rt == ResetType.NEVER) {
                homeScreen.assertNoHeaderExists()
            } else {
                homeScreen.assertFirstHeaderTitleIs(rt.headerTitle)
            }
        }
    }

    @Test
    fun groupTypesCurrentGroupHeaderTitle() {
        val homeScreen = HomeScreenTestView(rule)
            .openNewCounterModal()
            .setRandomName()
            .create()
            .openNewEntryModalOfCounterIncrease()
            .addEntry()
            .openCounter()

        for (rt in ResetType.values()) {
            homeScreen
                .setGroupTypeOfResetType(rt)

            if (rt == ResetType.NEVER) {
                homeScreen.assertNoHeaderExists()
            } else {
                homeScreen.assertFirstHeaderTitleIs(rt.headerTitle)
            }
        }
    }

    @Test
    fun openSettings() {
        HomeScreenTestView(rule)
            .openSettings()
            .assertTitleExists()

    }

    @Test
    fun openDataSettings() {
        HomeScreenTestView(rule)
            .openSettings()
            .openDataSettingsScreen()
            .assertTitleExists()
    }

    @Test
    fun openCardSettings() {
        HomeScreenTestView(rule)
            .openNewCounterModal()
            .setRandomName()
            .create()
            .openCounter()
            .openSettingsTab()
            .openCardSettings()
            .assertTitleExists()
    }

    /*@Test
    fun test() {
        HomeScreenTestView(rule)
            .createFRScreenshotCounters()
    }*/
}