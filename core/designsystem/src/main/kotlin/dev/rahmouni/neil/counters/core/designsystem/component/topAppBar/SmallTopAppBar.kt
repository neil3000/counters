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

package dev.rahmouni.neil.counters.core.designsystem.component.topAppBar

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.AutoMirrored.Outlined
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import dev.rahmouni.neil.counters.core.designsystem.DropdownMenu
import dev.rahmouni.neil.counters.core.designsystem.R
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentVariation
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Rn3SmallTopAppBar(
    modifier: Modifier = Modifier,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackIconButtonClicked: (() -> Unit)? = null,
    actions: List<TopAppBarAction> = emptyList(),
    transparent: Boolean = false,
    title: @Composable () -> Unit,
) {
    TopAppBar(
        title = title,
        modifier = modifier,
        navigationIcon = {
            if (onBackIconButtonClicked != null) {
                Rn3IconButton(
                    icon = Outlined.ArrowBack,
                    contentDescription = stringResource(R.string.core_designsystem_largeTopAppBar_navigationIcon_iconButton_arrowBack_contentDescription),
                    onClick = onBackIconButtonClicked,
                )
            }
        },
        actions = {
            when (actions.size) {
                0 -> Unit
                1 -> actions[0].IconButton()
                else -> {
                    var expanded by remember { mutableStateOf(false) }

                    Rn3IconButton(
                        icon = Icons.Outlined.MoreVert,
                        contentDescription = "See more",
                    ) { expanded = true }

                    actions.DropdownMenu(expanded = expanded) { expanded = false }
                }
            }
        },
        windowInsets = windowInsets,
        scrollBehavior = scrollBehavior,
        colors = if (transparent) {
            TopAppBarDefaults.topAppBarColors().copy(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent,
            )
        } else {
            TopAppBarDefaults.topAppBarColors()
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Rn3SmallTopAppBar(
            title = { Text("Preview default") },
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Rn3PreviewComponentVariation
@Composable
private fun BackArrow() {
    Rn3Theme {
        Rn3SmallTopAppBar(
            title = { Text("Preview back button") },
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        )
    }
}
