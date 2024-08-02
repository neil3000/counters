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

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.roundedCorners.Rn3RoundedCorners
import dev.rahmouni.neil.counters.feature.login.R.string

@Composable
internal fun AnonymousTile(shape: Rn3RoundedCorners, onClick: () -> Unit) {
    Surface(
        tonalElevation = Rn3ExpandableSurfaceDefaults.tonalElevation,
        shape = shape.toComposeShape(),
    ) {
        Rn3TileClick(
            title = stringResource(string.feature_login_withoutAccount),
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.NoAccounts,
                    contentDescription = null,
                    modifier = Modifier
                        .size(37.dp)
                        .padding(horizontal = 2.dp),
                )
            },
            supportingContent = { Text(text = stringResource(string.feature_login_SignInLater)) },
            trailingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.KeyboardArrowRight,
                    contentDescription = null,
                )
            },
            onClick = onClick,
        )
    }
}
