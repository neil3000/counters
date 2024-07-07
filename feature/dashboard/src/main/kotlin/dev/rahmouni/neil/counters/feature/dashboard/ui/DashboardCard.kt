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

package dev.rahmouni.neil.counters.feature.dashboard.ui

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.AnimatedNumber
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.feature.dashboard.R
import dev.rahmouni.neil.counters.feature.dashboard.model.CounterEntity
import java.text.NumberFormat

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CounterEntity.DashboardCard(
    modifier: Modifier = Modifier,
    onIncrement: (value: Long) -> Unit,
) {
    val haptics = getHaptic()
    val context = LocalContext.current
    val displayData = getDisplayData()

    val showRemoveButton = false
    val showAddButton = true

    Card(
        colors = CardDefaults.cardColors(getColor()),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .combinedClickable(
                    onClick = {
                        haptics.click()

                        Toast
                            .makeText(context, uid, Toast.LENGTH_SHORT)
                            .show()
                        // TODO("Not implemented - add counter page")
                    },
                    onLongClick = {
                        haptics.longPress()

                        // TODO("Not implemented - add long clicks")
                    },
                )
                .padding(2.dp),
        ) {
            Text(
                text = getTitle(),
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(start = 12.dp, top = 8.dp, end = 12.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                if (showRemoveButton) {
                    Rn3IconButton(
                        icon = Icons.Outlined.Remove,
                        contentDescription = stringResource(R.string.feature_dashboard_dashboardCard_removeButton_contentDescription),
                        onClick = { onIncrement(-1) },
                    )
                }

                Row(
                    modifier = Modifier
                        .padding(
                            start = if (showRemoveButton) 0.dp else 12.dp,
                            end = if (showAddButton) 0.dp else 12.dp,
                        )
                        .weight(1f),
                ) {
                    displayData.forEach { (value, unit) ->
                        AnimatedNumber(
                            currentValue = value,
                            modifier = Modifier
                                .weight(1f, fill = false)
                                .align(Alignment.Bottom),
                        ) { targetValue ->
                            Text(
                                text = NumberFormat.getNumberInstance().format(targetValue),
                                style = MaterialTheme.typography.headlineLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                            )
                        }

                        if (unit != null) {
                            Text(
                                text = unit,
                                softWrap = false,
                                modifier = Modifier
                                    .padding(start = 2.dp)
                                    .alpha(0.85f)
                                    .align(getAlignment()),
                            )
                        }
                    }
                }

                if (showAddButton) {
                    Rn3IconButton(
                        icon = Icons.Outlined.Add,
                        contentDescription = stringResource(R.string.feature_dashboard_dashboardCard_addButton_contentDescription),
                        onClick = { onIncrement(1) },
                    )
                }
            }
        }
    }
}
