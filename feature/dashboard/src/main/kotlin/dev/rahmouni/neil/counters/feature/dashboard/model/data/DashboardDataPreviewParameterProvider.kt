/*
 * Copyright 2024 Rahmouni Neïl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
                unit = UnitRepository.findUnitByVariableName("GRAM"),
                prefix = 3,
                currentValue = 30,
            ),
            CounterEntity(
                uid = "id2",
                title = "Coffee cups",
                color = "PRIMARY",
                unit = UnitRepository.findUnitByVariableName("BYTE"),
                prefix = null,
                currentValue = 2,
            ),
        ),
    )
    val dashboardData_mutations = with(dashboardData_default) {
        sequenceOf(
            copy(counters = emptyList()),
        )
    }
}
