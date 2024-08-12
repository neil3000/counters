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
            Text(
                modifier = Modifier.padding(Rn3TextDefaults.paddingValues.only(HORIZONTAL)),
                text = friendEntity.display(),
            )
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
                    Text(text = stringResource(string.feature_connect_friendTileClick_button))
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
