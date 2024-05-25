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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.window.core.layout.WindowHeightSizeClass.Companion.COMPACT
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.topAppBar.Rn3LargeTopAppBar
import dev.rahmouni.neil.counters.core.designsystem.component.topAppBar.Rn3SmallTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rn3Scaffold(
    modifier: Modifier = Modifier,
    topAppBarTitle: String,
    onBackIconButtonClicked: (() -> Unit)?,
    topAppBarActions: List<TopAppBarAction> = emptyList(),
    topAppBarStyle: TopAppBarStyle = TopAppBarStyle.LARGE,
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit,
) {
    val windowHeightClass = currentWindowAdaptiveInfo().windowSizeClass.windowHeightSizeClass

    when {
        // Large
        topAppBarStyle == TopAppBarStyle.LARGE && windowHeightClass != COMPACT -> Rn3ScaffoldImpl(
            modifier,
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            content,
            floatingActionButton,
        ) { scrollBehavior ->
            Rn3LargeTopAppBar(
                modifier,
                topAppBarTitle,
                scrollBehavior = scrollBehavior,
                onBackIconButtonClicked = onBackIconButtonClicked,
                actions = topAppBarActions,
            )
        }

        // Small
        else -> Rn3ScaffoldImpl(
            modifier,
            TopAppBarDefaults.pinnedScrollBehavior(),
            content,
            floatingActionButton,
        ) { scrollBehavior ->
            Rn3SmallTopAppBar(
                modifier,
                topAppBarTitle,
                scrollBehavior = scrollBehavior,
                onBackIconButtonClicked = onBackIconButtonClicked,
                actions = topAppBarActions,
            )
        }
    }
}

@SuppressLint("DesignSystem")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Rn3ScaffoldImpl(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable (PaddingValues) -> Unit,
    floatingActionButton: @Composable () -> Unit,
    topBarComponent: @Composable (scrollBehavior: TopAppBarScrollBehavior) -> Unit,
) {
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { topBarComponent(scrollBehavior) },
        contentWindowInsets = WindowInsets.displayCutout,
        floatingActionButton = floatingActionButton,
    ) {
        Column {
            content(it)
        }
    }
}

enum class TopAppBarStyle {
    SMALL,
    LARGE,
}
