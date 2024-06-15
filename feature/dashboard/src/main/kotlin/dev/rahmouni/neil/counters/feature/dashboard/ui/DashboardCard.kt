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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.AnimatedNumber
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.feature.dashboard.R
import dev.rahmouni.neil.counters.feature.dashboard.model.CounterEntity

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun CounterEntity.DashboardCard(
    modifier: Modifier = Modifier,
    onIncrement: () -> Unit,
) {
    val haptics = getHaptic()
    val context = LocalContext.current
    val displayData = getDisplayData()

    Card(
        colors = CardDefaults.cardColors(getColor()),
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.combinedClickable(
                onClick = {
                    haptics.click()

                    Toast.makeText(context, uid, Toast.LENGTH_SHORT).show()
                    // TODO("Not implemented - add counter page")
                },
                onLongClick = {
                    haptics.longPress()

                    // TODO("Not implemented - add long clicks")
                },
            ),
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
                modifier = Modifier
                    .fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .weight(1f),
                ) {
                    AnimatedNumber(
                        currentValue = currentValue,
                        modifier = Modifier.weight(1f, fill = false),
                    ) {
                        Text(
                            text = displayData.first,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.headlineLarge,
                            softWrap = false,
                            modifier = Modifier
                                .alpha(0.85f),
                        )
                    }

                    Text(
                        text = displayData.second,
                        softWrap = false,
                        modifier = Modifier
                            .alpha(0.85f)
                            .align(getAlignment()),
                    )
                }

                Rn3IconButton(
                    icon = Icons.Outlined.Add,
                    contentDescription = stringResource(R.string.feature_dashboard_dashboardCard_addButton_contentDescription),
                    onClick = onIncrement,
                    modifier = Modifier.align(androidx.compose.ui.Alignment.CenterVertically),
                )
            }
        }
    }
}
