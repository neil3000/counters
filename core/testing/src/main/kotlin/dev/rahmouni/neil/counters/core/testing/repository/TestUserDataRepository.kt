package dev.rahmouni.neil.counters.core.testing.repository

import dev.rahmouni.neil.counters.core.data.repository.UserDataRepository
import dev.rahmouni.neil.counters.core.model.data.UserData
import kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterNotNull

val emptyUserData = UserData(
    hasAccessibilityEmphasizedSwitchesEnabled = false,
    hasAccessibilityIconTooltipsEnabled = true,
    hasMetricsEnabled = true,
    hasCrashlyticsEnabled = true,
)

class TestUserDataRepository : UserDataRepository {
    /**
     * The backing hot flow for the list of followed topic ids for testing.
     */
    private val _userData = MutableSharedFlow<UserData>(replay = 1, onBufferOverflow = DROP_OLDEST)

    private val currentUserData get() = _userData.replayCache.firstOrNull() ?: emptyUserData

    override val userData: Flow<UserData> = _userData.filterNotNull()

    override suspend fun setAccessibilityEmphasizedSwitches(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(hasAccessibilityEmphasizedSwitchesEnabled = value))
        }
    }

    override suspend fun setAccessibilityIconTooltips(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(hasAccessibilityIconTooltipsEnabled = value))
        }
    }

    override suspend fun setMetricsEnabled(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(hasMetricsEnabled = value))
        }
    }

    override suspend fun setCrashlyticsEnabled(value: Boolean) {
        currentUserData.let { current ->
            _userData.tryEmit(current.copy(hasCrashlyticsEnabled = value))
        }
    }

    /**
     * A test-only API to allow setting of user data directly.
     */
    fun setUserData(userData: UserData) {
        _userData.tryEmit(userData)
    }
}
