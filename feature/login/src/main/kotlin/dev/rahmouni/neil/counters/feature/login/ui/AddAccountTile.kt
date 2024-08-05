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

package dev.rahmouni.neil.counters.feature.login.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3AdditionalBigPadding
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.rn3ShrinkVerticallyTransition
import dev.rahmouni.neil.counters.core.designsystem.roundedCorners.Rn3RoundedCorners
import dev.rahmouni.neil.counters.feature.login.R.string

@Composable
internal fun AddAccountTile(expanded: Boolean, shape: Rn3RoundedCorners, onClick: () -> Unit) {
    val haptic = getHaptic()

    Surface(
        tonalElevation = Rn3SurfaceDefaults.tonalElevation,
        shape = shape.toComposeShape(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    haptic.click()
                    onClick()
                }
                .padding(Rn3AdditionalBigPadding.paddingValues),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = stringResource(string.feature_login_addAccount))
                AnimatedVisibility(
                    visible = expanded,
                    exit = rn3ShrinkVerticallyTransition(),
                ) {
                    Text(
                        text = stringResource(string.feature_login_SingIntoSync),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }
    }
}
