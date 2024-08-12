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

package dev.rahmouni.neil.counters.core.designsystem.component.tile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.R.string
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Dialog
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3TextDefaults
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.END
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.toRn3FormattedString

@Composable
fun Rn3TileClickConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    bodyHeader: String,
    bodyBulletPoints: Map<ImageVector, String>,
    supportingText: String? = null,
    enabled: Boolean = true,
    error: Boolean = true,
    onClick: () -> Unit,
) {
    Rn3TileClickConfirmationDialog(
        modifier = modifier,
        title = title,
        icon = icon,
        supportingText = supportingText,
        enabled = enabled,
        error = error,
        onClick = onClick,
    ) {
        Column {
            Text(
                text = bodyHeader,
                modifier = Modifier.padding(Rn3TextDefaults.paddingValues.only(END)),
            )
            bodyBulletPoints.forEach { (icon, text) ->
                Row {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(
                                Rn3TextDefaults.paddingValues.copy(
                                    end = 0.dp,
                                    bottom = 0.dp,
                                ),
                            )
                            .size(SuggestionChipDefaults.IconSize),
                        tint = MaterialTheme.colorScheme.secondary,
                    )

                    Text(
                        text = text.toRn3FormattedString(),
                        modifier = Modifier.padding(Rn3TextDefaults.paddingValues.copy(bottom = 0.dp)),
                    )
                }
            }
        }
    }
}

@Composable
fun Rn3TileClickConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    supportingText: String? = null,
    enabled: Boolean = true,
    error: Boolean = true,
    onClick: () -> Unit,
    body: @Composable () -> Unit,
) {
    Rn3Dialog(
        icon = icon,
        title = stringResource(string.core_designsystem_confirmationDialog_title),
        body = body,
        confirmLabel = title,
        onConfirm = onClick,
    ) {
        Rn3TileClick(
            modifier = modifier,
            title = title,
            icon = icon,
            supportingText = supportingText,
            enabled = enabled,
            error = error,
            onClick = it,
        )
    }
}
