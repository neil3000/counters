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

package dev.rahmouni.neil.counters.core.designsystem.component.user

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.NoAccounts
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import dev.rahmouni.neil.counters.core.user.Rn3User
import dev.rahmouni.neil.counters.core.user.Rn3User.SignedInUser

@Composable
fun Rn3User.Avatar() {
    when (this@Avatar) {
        is SignedInUser -> SubcomposeAsyncImage(
            model = this@Avatar.pfpUri,
            contentDescription = null,
            loading = { FallbackPfp() },
            error = { FallbackPfp() },
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape),
        )

        else -> Icon(imageVector = Icons.Outlined.NoAccounts, contentDescription = null)
    }
}

@Composable
private fun FallbackPfp() {
    Icon(imageVector = Icons.Outlined.AccountCircle, contentDescription = null)
}
