package dev.rahmouni.neil.counters.core.data.repository

import dev.rahmouni.neil.counters.core.model.data.UserData
import kotlinx.coroutines.flow.Flow

interface UserDataRepository {
    /**
     * Stream of [UserData]
     */
    val userData: Flow<UserData>

    /**
     * Sets whether the user has enabled the accessibility settings that emphasizes selected switches
     */
    suspend fun setAccessibilityEmphasizedSwitches(value: Boolean)

    /**
     * Sets whether the user has enabled the accessibility settings that adds tooltips to Icons
     */
    suspend fun setAccessibilityIconTooltips(value: Boolean)

    suspend fun setMetricsEnabled(value: Boolean)
    suspend fun setCrashlyticsEnabled(value: Boolean)
}