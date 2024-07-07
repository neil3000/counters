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
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    contentColor: Color = Color.Unspecified,
    onClick: () -> Unit,
) {
    val accessibilityHelper = LocalAccessibilityHelper.current

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
            Rn3IconButtonImpl(
                modifier = modifier,
                icon = icon,
                contentDescription = contentDescription,
                contentColor = contentColor,
                onClick = onClick,
            )
        }
    } else {
        Rn3IconButtonImpl(
            modifier = modifier,
            icon = icon,
            contentDescription = contentDescription,
            contentColor = contentColor,
            onClick = onClick,
        )
    }
}

@Composable
private fun Rn3IconButtonImpl(
    modifier: Modifier,
    icon: ImageVector,
    contentDescription: String,
    contentColor: Color,
    onClick: () -> Unit,
) {
    val haptic = getHaptic()

    IconButton(
        onClick = {
            onClick()
            haptic.click()
        },
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors().copy(
            contentColor = contentColor,
        ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
        )
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
