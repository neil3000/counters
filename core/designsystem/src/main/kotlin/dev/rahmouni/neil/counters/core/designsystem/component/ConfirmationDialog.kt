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

package dev.rahmouni.neil.counters.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.R
import dev.rahmouni.neil.counters.core.designsystem.rn3ErrorContainer

@Composable
fun Rn3ConfirmationDialog(
    icon: ImageVector,
    body: @Composable () -> Unit,
    confirmLabel: String,
    onConfirm: () -> Unit,
    content: @Composable (openDialog: () -> Unit) -> Unit,
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }

    content {
        openDialog = true
    }

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            title = {
                Text(text = stringResource(R.string.core_designsystem_confirmationDialog_title))
            },
            icon = {
                Icon(
                    icon,
                    null,
                )
            },
            text = body,
            confirmButton = {
                Column(Modifier.fillMaxWidth(), Arrangement.spacedBy(8.dp)) {
                    Rn3LargeButton(
                        text = stringResource(R.string.core_designsystem_confirmationDialog_cancelButton_text),
                        icon = Icons.Outlined.Close,
                    ) {
                        openDialog = false
                    }
                    Rn3LargeButton(
                        confirmLabel,
                        icon,
                        color = MaterialTheme.colorScheme.rn3ErrorContainer,
                    ) {
                        openDialog = false

                        onConfirm()
                    }
                }
            },
        )
    }
}
