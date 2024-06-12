/*
 * Copyright 2024 Rahmouni Neïl
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

package dev.rahmouni.neil.counters.core.auth.user

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.unit.dp

@Composable
fun Rn3User.UserAvatarAndName(modifier: Modifier = Modifier, showEmail: Boolean = false) {
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Avatar()
        Column(Modifier.padding(top = 2.dp, start = 16.dp, end = 16.dp)) {
            Text(
                getDisplayName(),
                style = MaterialTheme.typography.titleMedium,
            )

            if (getEmailAddress() != null && showEmail) {
                Text(
                    getEmailAddress()!!,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
        if (isAdmin()) {
            Icon(
                Icons.Outlined.VerifiedUser,
                null,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(FilterChipDefaults.IconSize),
                tint = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}
