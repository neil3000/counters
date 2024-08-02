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

package dev.rahmouni.neil.counters.core.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeaderDefaults
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.rn3ExpandVerticallyTransition

@Composable
internal fun FeedbackMessages(messages: List<String>) {
    var trigger by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        trigger = true
    }

    Spacer(modifier = Modifier.height(16.dp))

    Column(
        modifier = Modifier.padding(Rn3ExpandableSurfaceDefaults.paddingValues),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        messages.forEachIndexed { index, message ->
            AnimatedVisibility(
                visible = trigger,
                enter = if (index > 0) {
                    rn3ExpandVerticallyTransition(
                        fadeDelay = index * 1000 + 250,
                        fadeDuration = 250,
                        expandDelay = index * 1000,
                        expandDuration = 250,
                    )
                } else {
                    EnterTransition.None
                },
            ) {
                Surface(
                    tonalElevation = Rn3ExpandableSurfaceDefaults.tonalElevation,
                    shape = RoundedCornerShape(8.dp),
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(Rn3TileSmallHeaderDefaults.paddingValues),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        }
    }
}
