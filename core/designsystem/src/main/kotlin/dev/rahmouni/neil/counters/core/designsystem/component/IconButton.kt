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
