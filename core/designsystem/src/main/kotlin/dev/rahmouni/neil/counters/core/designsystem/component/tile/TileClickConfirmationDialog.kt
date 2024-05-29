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

package dev.rahmouni.neil.counters.core.designsystem.component.tile

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ConfirmationDialog

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Rn3TileClickConfirmationDialog(
    modifier: Modifier = Modifier,
    title: String,
    icon: ImageVector,
    body: String,
    supportingText: String? = null,
    enabled: Boolean = true,
    error: Boolean = true,
    onClick: () -> Unit,
) {
    Rn3ConfirmationDialog(
        title = title,
        icon = icon,
        body = {
            Text(body)
        },
        confirmLabel = "Logout",
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