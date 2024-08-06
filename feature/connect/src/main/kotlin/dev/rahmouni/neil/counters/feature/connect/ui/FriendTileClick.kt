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

package dev.rahmouni.neil.counters.feature.connect.ui

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.data.model.FriendEntity
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3TextDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.HORIZONTAL
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.feature.connect.R.string

@Composable
fun Rn3FriendTileClick(
    modifier: Modifier = Modifier,
    icon: ImageVector = Outlined.AccountCircle,
    button: Boolean = false,
    friendEntity: FriendEntity,
) {
    val haptic = getHaptic()
    val context = LocalContext.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp, bottom = 4.dp, start = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row {
            Icon(imageVector = icon, contentDescription = null)
            Text(modifier = Modifier.padding(Rn3TextDefaults.paddingValues.only(HORIZONTAL)), text = friendEntity.display())
        }
        Row {
            if (button && friendEntity.nearby) {
                OutlinedButton(
                    onClick = {
                        haptic.click()

                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = Uri.parse("sms:${friendEntity.formatPhone()}")
                        }

                        context.startActivity(intent)
                    },
                ) {
                    Text(stringResource(string.feature_connect_friendTileClick_button))
                }
            }
            Rn3IconButton(
                icon = Icons.Default.ChevronRight,
                contentDescription = stringResource(string.feature_connect_friendTileClick_iconButton_contentDescription),
                onClick = {
                    haptic.click()

                    Toast
                        .makeText(
                            context,
                            friendEntity.display(),
                            Toast.LENGTH_SHORT,
                        )
                        .show()
                },
            )
        }
    }
}
