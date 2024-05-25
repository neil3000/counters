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
import dev.rahmouni.neil.counters.core.data.model.CounterData
import dev.rahmouni.neil.counters.feature.dashboard.model.data.PreviewParameterData.dashboardData_default
import dev.rahmouni.neil.counters.feature.dashboard.model.data.PreviewParameterData.dashboardData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [DashboardData] for Composable previews.
 */
class DataAndPrivacySettingsDataPreviewParameterProvider :
    PreviewParameterProvider<DashboardData> {
    override val values: Sequence<DashboardData> =
        sequenceOf(dashboardData_default).plus(dashboardData_mutations)
}

object PreviewParameterData {
    val dashboardData_default = DashboardData(
        counters = listOf(
            CounterData("id1", "Push-ups"),
            CounterData("id2", "Coffee cups"),
        ),
        lastUserUid = "unset",
    )
    val dashboardData_mutations = dashboardData_default.let {
        sequenceOf(
            it.copy(counters = emptyList()),
        )
    }
}
