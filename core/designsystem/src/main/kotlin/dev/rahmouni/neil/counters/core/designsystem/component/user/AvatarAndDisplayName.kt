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
