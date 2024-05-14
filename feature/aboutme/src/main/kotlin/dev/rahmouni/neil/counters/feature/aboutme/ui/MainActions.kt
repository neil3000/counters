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

package dev.rahmouni.neil.counters.feature.aboutme.ui

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.common.openLink
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClickChips
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.icons.CoffeeAnimated
import dev.rahmouni.neil.counters.feature.aboutme.R
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PortfolioState
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PortfolioState.Available
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PortfolioState.InMaintenance
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PortfolioState.SoonAvailable

@Composable
fun MainActions(portfolio: PortfolioState) {
    val context = LocalContext.current

    fun soonToast() {
        Toast.makeText(context, "Soon ~", Toast.LENGTH_LONG).show()
    }

    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        Column {
            // portfolioTile
            Rn3TileClick(
                title = stringResource(R.string.feature_aboutme_mainActions_portfolioTile_title),
                icon = Icons.Outlined.Language,
                supportingText = when (portfolio) {
                    is InMaintenance -> stringResource(R.string.feature_aboutme_mainActions_portfolioTile_supportingText_inMaintenance)
                    is SoonAvailable -> stringResource(R.string.feature_aboutme_mainActions_portfolioTile_supportingText_soonAvailable)
                    else -> stringResource(R.string.feature_aboutme_mainActions_portfolioTile_supportingText)
                },
                external = true,
                enabled = portfolio is Available,
            ) {
                if (portfolio is Available) context.openLink(portfolio.uri)
            }

            Rn3TileHorizontalDivider(color = MaterialTheme.colorScheme.secondary)

            // buyMeCoffeeTile //TODO logic
            Rn3TileClickChips(
                title = stringResource(R.string.feature_aboutme_mainActions_buyMeCoffeeTile_title),
                icon = Icons.Outlined.CoffeeAnimated,
                onClick = ::soonToast
            ) {
                items(listOf("1 €", "3 €", "5 €", "Custom")) {
                    FilterChip(
                        selected = it == "3 €",
                        onClick = ::soonToast,
                        label = { Text(it) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiary,
                        ),
                    )
                }
            }
        }
    }
}
