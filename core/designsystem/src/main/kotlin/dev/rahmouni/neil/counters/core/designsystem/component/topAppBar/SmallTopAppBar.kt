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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
    title: String,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    scrollBehavior: TopAppBarScrollBehavior,
    onBackIconButtonClicked: (() -> Unit)? = null,
    topAppBarActions: List<TopAppBarAction> = emptyList(),
) {
    TopAppBar(
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
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
            when (topAppBarActions.size) {
                0 -> Unit
                1 -> topAppBarActions[0].IconButton()
                else -> {
                    var expanded by remember { mutableStateOf(false) }

                    Rn3IconButton(
                        icon = Icons.Outlined.MoreVert,
                        contentDescription = "See more",
                    ) { expanded = true }

                    topAppBarActions.DropdownMenu(expanded = expanded) { expanded = false }
                }
            }
        },
        windowInsets = windowInsets,
        scrollBehavior = scrollBehavior,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Rn3SmallTopAppBar(
            title = "Preview default",
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
            title = "Preview back button",
            scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
        )
    }
}