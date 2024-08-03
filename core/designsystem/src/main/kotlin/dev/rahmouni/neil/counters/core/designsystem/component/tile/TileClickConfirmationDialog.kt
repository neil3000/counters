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

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
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
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) { //TODO: rework spacing
            Text(text = bodyHeader)
            bodyBulletPoints.forEach { (icon, text) ->
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Spacer(modifier = Modifier.height(2.dp))

                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(SuggestionChipDefaults.IconSize),
                        tint = MaterialTheme.colorScheme.secondary,
                    )

                    Text(text = text.toRn3FormattedString())
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
