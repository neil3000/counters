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

package dev.rahmouni.neil.counters.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import dev.rahmouni.neil.counters.core.accessibility.LocalAccessibilityHelper
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme

@SuppressLint("DesignSystem")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rn3IconButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    contentDescription: String,
    tooltip: String? = contentDescription,
    onClick: () -> Unit,
) {
    val accessibilityHelper = LocalAccessibilityHelper.current
    val haptic = getHaptic()

    if (tooltip != null && !(tooltip == contentDescription && !accessibilityHelper.hasIconTooltipsEnabled)) {
        TooltipBox(
            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
            tooltip = {
                PlainTooltip {
                    Text(tooltip)
                }
            },
            state = rememberTooltipState(),
            focusable = false,
        ) {
            IconButton(
                onClick = {
                    onClick()
                    haptic.click()
                },
                modifier = modifier,
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = contentDescription,
                )
            }
        }
    } else {
        IconButton(
            onClick = {
                onClick()
                haptic.click()
            },
            modifier = modifier,
        ) {
            Icon(
                imageVector = icon,
                contentDescription = contentDescription,
            )
        }
    }
}

@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Rn3IconButton(
            icon = Outlined.EmojiEvents,
            contentDescription = "",
        ) {}
    }
}
