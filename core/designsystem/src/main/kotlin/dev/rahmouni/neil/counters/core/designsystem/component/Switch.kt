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
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import dev.rahmouni.neil.counters.core.accessibility.AccessibilityHelper
import dev.rahmouni.neil.counters.core.accessibility.LocalAccessibilityHelper
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentVariation
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme

@SuppressLint("DesignSystem")
@Composable
fun Rn3Switch(
    modifier: Modifier = Modifier,
    checked: Boolean,
    contentDescription: String?,
    enabled: Boolean = true,
    thumbContent: @Composable (() -> Unit)? = null,
    interactionSource: MutableInteractionSource? = null,
    onCheckedChange: ((Boolean) -> Unit)?,
) {
    val accessibilityHelper = LocalAccessibilityHelper.current
    val haptic = getHaptic()

    Switch(
        checked = checked,
        onCheckedChange = if (onCheckedChange != null) {
            {
                onCheckedChange(it)
                haptic.toggle(it)
            }
        } else {
            null
        },
        modifier = if (contentDescription == null) {
            modifier
        } else {
            modifier.semantics {
                this.contentDescription = contentDescription
            }
        },
        thumbContent = thumbContent
            ?: if (accessibilityHelper.hasEmphasizedSwitchesEnabled && checked) {
                { Rn3SwitchAccessibilityEmphasizedThumbContent() }
            } else {
                null
            },
        enabled = enabled,
        interactionSource = interactionSource ?: remember { MutableInteractionSource() },
    )
}

@VisibleForTesting
@Composable
fun Rn3SwitchAccessibilityEmphasizedThumbContent() {
    Icon(
        imageVector = Icons.Outlined.Check,
        contentDescription = null,
        modifier = Modifier.size(SwitchDefaults.IconSize),
    )
}

// Previews

@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Rn3Switch(checked = true, contentDescription = null) {}
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun Off() {
    Rn3Theme {
        Rn3Switch(checked = false, contentDescription = null) {}
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun OnDisabled() {
    Rn3Theme {
        Rn3Switch(checked = true, enabled = false, contentDescription = null) {}
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun OffDisabled() {
    Rn3Theme {
        Rn3Switch(checked = false, enabled = false, contentDescription = null) {}
    }
}

@Rn3PreviewComponentVariation
@Composable
private fun OnAccessibility() {
    CompositionLocalProvider(
        value = LocalAccessibilityHelper provides AccessibilityHelper(hasEmphasizedSwitchesEnabled = true),
    ) {
        Rn3Theme {
            Rn3Switch(checked = true, contentDescription = null) {}
        }
    }
}
