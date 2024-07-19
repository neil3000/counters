/*
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.feature.events.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.feature.events.model.EventsUiState.Success
import dev.rahmouni.neil.counters.feature.events.model.data.EventsData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class EventsViewModel @Inject constructor(
    authHelper: AuthHelper,
) : ViewModel() {

    val uiState: StateFlow<EventsUiState> =
        authHelper.getUserFlow().map { user ->
            Success(
                EventsData(
                    user = user,
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = Success(
                EventsData(
                    user = authHelper.getUser(),
                ),
            ),
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )
}
