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

package dev.rahmouni.neil.counters.core.feedback

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic

@Composable
internal fun FeedbackOptions(
    options: Map<String, String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
) {
    val haptic = getHaptic()

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    ) {
        options.forEach { (key, title) ->
            ExtendedFloatingActionButton(
                text = { Text(title, color = MaterialTheme.colorScheme.onSurfaceVariant) },
                icon = {
                    AnimatedVisibility(
                        visible = selectedOption == key,
                        enter = fadeIn() + scaleIn() + expandHorizontally(),
                        exit = fadeOut() + scaleOut() + shrinkHorizontally(),
                    ) {
                        Icon(Icons.Outlined.Check, null)
                    }
                },
                onClick = {
                    haptic.click()
                    onOptionSelected(key)
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                containerColor = if (key == selectedOption) FloatingActionButtonDefaults.containerColor else MaterialTheme.colorScheme.surfaceVariant,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),
            )
        }
    }
}
