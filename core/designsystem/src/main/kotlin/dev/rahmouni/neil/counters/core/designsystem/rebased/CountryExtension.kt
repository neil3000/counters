/*
 * Copyright (C) 2024 Rahmouni Neïl & Aurélien Coppée
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

package dev.rahmouni.neil.counters.core.designsystem.rebased

import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.core.designsystem.R.string
import dev.rahmouni.neil.counters.core.designsystem.icons.Belgium
import dev.rahmouni.neil.counters.core.designsystem.icons.France
import dev.rahmouni.neil.counters.core.designsystem.icons.UK
import dev.rahmouni.neil.counters.core.designsystem.icons.USA
import dev.rahmouni.neil.counters.core.model.data.Country

@Composable
fun Country.icon(): ImageVector = when (this) {
    Country.FRANCE -> Icons.Outlined.France
    Country.BELGIUM -> Icons.Outlined.Belgium
    Country.UNITED_KINGDOM -> Icons.Outlined.UK
    Country.USA -> Icons.Outlined.USA
}

@Composable
fun Country.text(): String = when (this) {
    Country.FRANCE -> stringResource(string.core_designsystem_countryText_france)
    Country.BELGIUM -> stringResource(string.core_designsystem_countryText_belgium)
    Country.UNITED_KINGDOM -> stringResource(string.core_designsystem_countryText_unitedKingdom)
    Country.USA -> stringResource(string.core_designsystem_countryText_unitedStates)
}

@Composable
fun Country.Companion.sortedCountries(): List<Country> {
    return Country.entries.sortedBy { country -> country.name }
}
