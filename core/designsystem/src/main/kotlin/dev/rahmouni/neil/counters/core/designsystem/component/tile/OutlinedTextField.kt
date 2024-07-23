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

package dev.rahmouni.neil.counters.core.designsystem.component.tile

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun Rn3OutlinedTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit,
    onEmptyStateChange: (Boolean) -> Unit,
    onTextChange: (() -> Unit)? = null,
    beEmpty: Boolean = false,
    maxCharacters: Int,
    label: @Composable() (() -> Unit)?,
    singleLine: Boolean = true,
) {
    var showEmpty by rememberSaveable { mutableStateOf(false) }
    var showTooLarge by rememberSaveable { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { text ->
            if (text.length <= maxCharacters) {
                onValueChange(text)
                showTooLarge = text.length >= maxCharacters
                showEmpty = text.isEmpty()
                onEmptyStateChange(showEmpty)
                onTextChange?.invoke()
            }
        },
        modifier = modifier,
        label = label,
        singleLine = singleLine,
        keyboardActions = KeyboardActions(
            onDone = {

            },
        ),
        supportingText = {
            if (showTooLarge) {
                Text(
                    "The limit of $maxCharacters characters has been reached",
                    color = MaterialTheme.colorScheme.error,
                )
            }

            if (showEmpty && !beEmpty) {
                Text(
                    "Please fill out this field.",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        },
    )
}
