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

package dev.rahmouni.neil.counters.core.designsystem.component.user

import android.content.res.Resources
import android.util.TypedValue
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.rn3ExpandVerticallyTransition
import dev.rahmouni.neil.counters.core.designsystem.rn3ShrinkVerticallyTransition
import dev.rahmouni.neil.counters.core.user.Rn3User

@Composable
fun Rn3User.UserAvatarAndName(modifier: Modifier = Modifier, showEmail: Boolean = false) {
    val context = LocalContext.current

    Row(
        modifier.height(
            with(LocalDensity.current) {
                TypedValue
                    .applyDimension(
                        TypedValue.COMPLEX_UNIT_SP,
                        20f * 2,
                        Resources.getSystem().displayMetrics,
                    )
                    .toDp() + 4.dp
            },
        ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar()
        Column(Modifier.padding(top = 2.dp, start = 16.dp, end = 16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    getDisplayName(context),
                    style = MaterialTheme.typography.titleMedium,
                )
                if (isAdmin()) {
                    Icon(
                        Icons.Outlined.VerifiedUser,
                        null,
                        modifier = Modifier
                            .padding(start = 4.dp)
                            .size(FilterChipDefaults.IconSize),
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                }
            }

            AnimatedVisibility(
                visible = getEmailAddress() != null && showEmail,
                enter = rn3ExpandVerticallyTransition(),
                exit = rn3ShrinkVerticallyTransition(),
            ) {
                Text(
                    getEmailAddress().toString(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
