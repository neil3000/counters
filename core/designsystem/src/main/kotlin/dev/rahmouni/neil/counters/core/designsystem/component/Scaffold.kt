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
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ExposurePlus2
import androidx.compose.material.icons.outlined.PlusOne
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass.Companion.COMPACT
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.BuildConfig
import dev.rahmouni.neil.counters.core.designsystem.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.DASHBOARD
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.LARGE
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.SMALL
import dev.rahmouni.neil.counters.core.designsystem.component.topAppBar.Rn3LargeTopAppBar
import dev.rahmouni.neil.counters.core.designsystem.component.topAppBar.Rn3SmallTopAppBar
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3
import dev.rahmouni.neil.counters.core.designsystem.toRn3PaddingValues

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rn3Scaffold(
    modifier: Modifier = Modifier,
    topAppBarTitle: String,
    onBackIconButtonClicked: (() -> Unit)?,
    topAppBarActions: List<TopAppBarAction> = emptyList(),
    topAppBarStyle: TopAppBarStyle = LARGE,
    floatingActionButton: @Composable (Modifier) -> Unit = {},
    content: @Composable (Rn3PaddingValues) -> Unit,
) {
    val windowHeightClass = currentWindowAdaptiveInfo().windowSizeClass.windowHeightSizeClass
    val config = LocalConfigHelper.current

    when {
        // LARGE
        topAppBarStyle == LARGE && windowHeightClass != COMPACT -> Rn3ScaffoldImpl(
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

        // SMALL or DASHBOARD
        else -> Rn3ScaffoldImpl(
            modifier,
            TopAppBarDefaults.pinnedScrollBehavior(),
            content,
            floatingActionButton,
        ) { scrollBehavior ->
            Rn3SmallTopAppBar(
                modifier,
                scrollBehavior = scrollBehavior,
                onBackIconButtonClicked = onBackIconButtonClicked,
                actions = topAppBarActions,
            ) {
                Row(
                    horizontalArrangement = spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (topAppBarStyle == DASHBOARD) {
                        val ee1 = (1..1000).random() == 1 || config.getBoolean("ee_1_force")

                        Surface(
                            Modifier.size(36.dp),
                            color = Color(136, 18, 41),
                            shape = RoundedCornerShape(8.dp),
                        ) {
                            when {
                                BuildConfig.DEBUG -> Icon(
                                    Icons.Outlined.Rn3,
                                    null,
                                    Modifier.scale(.6f),
                                    tint = Color.White,
                                )

                                ee1 -> Icon(
                                    Icons.Outlined.ExposurePlus2,
                                    null,
                                    Modifier.scale(.75f),
                                    tint = Color.White,
                                )

                                else -> Icon(
                                    Icons.Outlined.PlusOne,
                                    null,
                                    Modifier.scale(.75f),
                                    tint = Color.White,
                                )
                            }
                        }
                    }
                    Text(
                        topAppBarTitle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }
}

@SuppressLint("DesignSystem", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun Rn3ScaffoldImpl(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable (Rn3PaddingValues) -> Unit,
    floatingActionButton: @Composable (Modifier) -> Unit,
    topBarComponent: @Composable (scrollBehavior: TopAppBarScrollBehavior) -> Unit,
) {
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { topBarComponent(scrollBehavior) },
        contentWindowInsets = WindowInsets.statusBars.add(WindowInsets.displayCutout),
        floatingActionButton = { floatingActionButton(Modifier.navigationBarsPadding()) },
    ) {
        content(
            it.toRn3PaddingValues() + WindowInsets.navigationBars.toRn3PaddingValues(),
        )
    }
}

/**
 * Styles to be applied to the TopAppBars ([Large][Rn3LargeTopAppBar] or [Small][Rn3SmallTopAppBar])
 *
 * • [LARGE] represents a classic [LargeTopAppBar][Rn3LargeTopAppBar] if the screen is high enough else falls back to a classic [SmallTopAppBar][Rn3SmallTopAppBar], this is the default behavior.
 *
 * • [SMALL] represents a classic [SmallTopAppBar][Rn3SmallTopAppBar]
 *
 * • [DASHBOARD] represents a classic [SmallTopAppBar][Rn3SmallTopAppBar] with the Counters Logo before the title.
 */
enum class TopAppBarStyle {
    LARGE,
    SMALL,
    DASHBOARD,
}
