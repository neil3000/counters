package dev.rahmouni.neil.counters.ui

import dagger.hilt.android.testing.HiltAndroidTest

/**
 * Tests all the navigation flows that are handled by the navigation library.
 */
@HiltAndroidTest
class NavigationTest {
    /*
        /**
         * Manages the components' state and is used to perform injection on your test
         */
        @get:Rule(order = 0)
        val hiltRule = HiltAndroidRule(this)

        /**
         * Create a temporary folder used to create a Data Store file. This guarantees that
         * the file is removed in between each test, preventing a crash.
         */
        @BindValue
        @get:Rule(order = 1)
        val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()


        /**
         * Use the primary activity to initialize the app normally.
         */
        @get:Rule(order = 2)
        val composeTestRule = createAndroidComposeRule<MainActivity>()

        private fun AndroidComposeTestRule<*, *>.stringResource(@StringRes resId: Int) =
            ReadOnlyProperty<Any, String> { _, _ -> activity.getString(resId) }

        // The strings used for matching in these tests
        private val settings by composeTestRule.stringResource(SettingsR.string.feature_settings_settingsScreen_topAppBar_title)

        @Before
        fun setup() = hiltRule.inject()

        @Test
        fun firstScreen_isSettings() {
            composeTestRule.apply {
                onNodeWithText(settings).assertExists()
            }
        }
        */
}
