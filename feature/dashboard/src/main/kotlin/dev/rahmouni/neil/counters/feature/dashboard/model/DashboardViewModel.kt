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

package dev.rahmouni.neil.counters.feature.dashboard.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.data.model.CounterRawData
import dev.rahmouni.neil.counters.core.data.model.IncrementRawData
import dev.rahmouni.neil.counters.core.data.repository.countersData.CountersDataRepository
import dev.rahmouni.neil.counters.feature.dashboard.model.data.DashboardData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val countersDataRepository: CountersDataRepository,
) : ViewModel() {

    internal val uiState: StateFlow<DashboardUiState> =
        countersDataRepository.userCounters.map { counters ->
            DashboardUiState(
                dashboardData = DashboardData(
                    counters = counters.sortedBy { it.createdAt }.map { it.toEntity() },
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = DashboardUiState(
                DashboardData(
                    counters = emptyList(),
                ),
            ),
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )

    fun createCounter(counterRawData: CounterRawData) {
        countersDataRepository.createCounter(counterRawData)
    }

    fun incrementCounter(counterUid: String, incrementRawData: IncrementRawData) {
        countersDataRepository.createIncrement(counterUid, incrementRawData)
    }
}
