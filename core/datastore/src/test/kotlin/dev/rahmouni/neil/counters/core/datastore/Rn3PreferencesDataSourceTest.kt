package dev.rahmouni.neil.counters.core.datastore

import dev.rahmouni.neil.counters.core.datastore.test.testUserPreferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class NiaPreferencesDataSourceTest {

    private val testScope = TestScope(UnconfinedTestDispatcher())

    private lateinit var subject: Rn3PreferencesDataSource

    @get:Rule
    val tmpFolder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @Before
    fun setup() {
        subject = Rn3PreferencesDataSource(
            tmpFolder.testUserPreferencesDataStore(testScope),
        )
    }

    @Test
    fun accessibilityEmphasizedSwitchesFalseByDefault() = testScope.runTest {
        assertFalse(subject.userData.first().accessibilityEmphasizedSwitches)
    }

    @Test
    fun userAccessibilityEmphasizedSwitchesIsTrueWhenSet() = testScope.runTest {
        subject.setAccessibilityEmphasizedSwitchesPreference(true)
        assertTrue(subject.userData.first().accessibilityEmphasizedSwitches)
    }
}
