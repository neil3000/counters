/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.core.data.repository.userData

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
    suspend fun setShouldShowLoginScreenOnStartup(value: Boolean)
    suspend fun setNotAppFirstLaunch()
}
