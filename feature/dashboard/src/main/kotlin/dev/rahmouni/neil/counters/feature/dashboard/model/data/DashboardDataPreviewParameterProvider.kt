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

package dev.rahmouni.neil.counters.feature.dashboard.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.feature.dashboard.model.CounterEntity
import dev.rahmouni.neil.counters.feature.dashboard.model.UnitRepository
import dev.rahmouni.neil.counters.feature.dashboard.model.data.PreviewParameterData.dashboardData_default
import dev.rahmouni.neil.counters.feature.dashboard.model.data.PreviewParameterData.dashboardData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [DashboardData] for Composable previews.
 */
internal class DashboardDataPreviewParameterProvider :
    PreviewParameterProvider<DashboardData> {
    override val values: Sequence<DashboardData> =
        sequenceOf(dashboardData_default).plus(dashboardData_mutations)
}

internal object PreviewParameterData {
    val dashboardData_default = DashboardData(
        counters = listOf(
            CounterEntity(
                uid = "id1",
                title = "Push-ups",
                color = "PRIMARY",
                unit = UnitRepository.findUnit("GRAM"),
                prefix = 3,
                currentValue = 30.0,
            ),
            CounterEntity(
                uid = "id2",
                title = "Coffee cups",
                color = "PRIMARY",
                unit = UnitRepository.findUnit("BYTE"),
                prefix = null,
                currentValue = 2.0,
            ),
        ),
    )
    val dashboardData_mutations = with(dashboardData_default) {
        sequenceOf(
            copy(counters = emptyList()),
        )
    }
}
