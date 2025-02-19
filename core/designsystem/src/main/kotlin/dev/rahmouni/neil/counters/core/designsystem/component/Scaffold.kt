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
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.core.ArcMode
import androidx.compose.animation.core.EaseInOutQuint
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.ExperimentalAnimationSpecApi
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
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
import dev.rahmouni.neil.counters.core.designsystem.LocalNavAnimatedVisibilityScope
import dev.rahmouni.neil.counters.core.designsystem.LocalSharedTransitionScope
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.DASHBOARD
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.LARGE
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.SMALL
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.TRANSPARENT
import dev.rahmouni.neil.counters.core.designsystem.component.topAppBar.Rn3LargeTopAppBar
import dev.rahmouni.neil.counters.core.designsystem.component.topAppBar.Rn3SmallTopAppBar
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.toRn3PaddingValues

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalSharedTransitionApi::class,
    ExperimentalAnimationSpecApi::class,
)
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

        // SMALL or DASHBOARD or TRANSPARENT
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
                transparent = topAppBarStyle == TRANSPARENT,
            ) {
                val sharedTransitionScope = LocalSharedTransitionScope.current
                    ?: throw IllegalStateException("RahNeil_N3:4F6o9kodw29Oaj8zoDlAWesB1Merqam9")
                val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current

                Row(
                    horizontalArrangement = spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    if (topAppBarStyle == DASHBOARD) {
                        val ee1 = (1..1000).random() == 1 || config.getBoolean("ee_1_force")

                        with(sharedTransitionScope) {
                            Surface(
                                Modifier
                                    .size(36.dp)
                                    .then(
                                        if (animatedVisibilityScope != null) {
                                            Modifier.sharedElement(
                                                rememberSharedContentState(key = "countersLogo_background"),
                                                animatedVisibilityScope = animatedVisibilityScope,
                                                boundsTransform = { initialBounds, targetBounds ->
                                                    keyframes {
                                                        initialBounds at 0 using ArcMode.ArcBelow using EaseInOutQuint
                                                        targetBounds at durationMillis
                                                    }
                                                },
                                            )
                                        } else {
                                            Modifier
                                        },
                                    ),
                                color = Color(136, 18, 41),
                                shape = RoundedCornerShape(8.dp),
                            ) {
                                Modifier
                                    .fillMaxSize()
                                    .skipToLookaheadSize()
                                    .then(
                                        if (animatedVisibilityScope != null) {
                                            with(animatedVisibilityScope) {
                                                Modifier
                                                    .animateEnterExit(
                                                        enter = slideInVertically(
                                                            animationSpec = tween(
                                                                delayMillis = 250,
                                                                easing = EaseOutBack,
                                                            ),
                                                        ) { it } + fadeIn(
                                                            tween(
                                                                durationMillis = 1,
                                                                delayMillis = 250,
                                                            ),
                                                        ),
                                                    )
                                                    .sharedElement(
                                                        rememberSharedContentState(key = "countersLogo_icon"),
                                                        animatedVisibilityScope = animatedVisibilityScope,
                                                        boundsTransform = { initialBounds, targetBounds ->
                                                            keyframes {
                                                                initialBounds at 0 using ArcMode.ArcBelow using EaseInOutQuint
                                                                targetBounds at durationMillis
                                                            }
                                                        },
                                                    )
                                            }
                                        } else {
                                            Modifier
                                        },
                                    ).let { modifier ->
                                        when {
                                            BuildConfig.DEBUG -> Icon(
                                                Icons.Outlined.Rn3,
                                                null,
                                                modifier.scale(.6f),
                                                tint = Color.White,
                                            )

                                            ee1 -> Icon(
                                                Icons.Outlined.ExposurePlus2,
                                                null,
                                                modifier.scale(.75f),
                                                tint = Color.White,
                                            )

                                            else -> Icon(
                                                Icons.Outlined.PlusOne,
                                                null,
                                                modifier.scale(.75f),
                                                tint = Color.White,
                                            )
                                        }
                                    }
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

@SuppressLint("DesignSystem")
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
 *
 * • [TRANSPARENT] is [SMALL] but with no background color on the appbar.
 */
enum class TopAppBarStyle {
    LARGE,
    SMALL,
    DASHBOARD,
    TRANSPARENT,
}
