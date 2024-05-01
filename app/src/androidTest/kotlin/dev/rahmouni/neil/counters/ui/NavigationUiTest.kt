package dev.rahmouni.neil.counters.ui

import dagger.hilt.android.testing.HiltAndroidTest

/**
 * Tests that the navigation UI is rendered correctly on different screen sizes.
 */
@HiltAndroidTest
class NavigationUiTest {
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
         * Use a test activity to set the content on.
         */
        @get:Rule(order = 2)
        val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()

        @Before
        fun setup() {
            hiltRule.inject()
        }

        /*@Composable
        private fun fakeAppState(maxWidth: Dp, maxHeight: Dp) = rememberCountersAppState(
           nav windowSizeClass = WindowSizeClass.calculateFromSize(DpSize(maxWidth, maxHeight)),
        )*/

     */
}
