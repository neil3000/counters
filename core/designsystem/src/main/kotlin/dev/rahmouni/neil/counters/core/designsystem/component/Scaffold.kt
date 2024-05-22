package dev.rahmouni.neil.counters.core.designsystem.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
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
    title: String,
    onBackIconButtonClicked: (() -> Unit)?,
    topAppBarActions: List<TopAppBarAction> = emptyList(),
    topAppBarStyle: TopAppBarStyle = TopAppBarStyle.LARGE,
    content: @Composable (PaddingValues) -> Unit,
) {
    val windowHeightClass = currentWindowAdaptiveInfo().windowSizeClass.windowHeightSizeClass

    when {
        // Large
        topAppBarStyle == TopAppBarStyle.LARGE && windowHeightClass != COMPACT -> Rn3ScaffoldImpl(
            modifier,
            TopAppBarDefaults.exitUntilCollapsedScrollBehavior(),
            content,
        ) { scrollBehavior ->
            Rn3LargeTopAppBar(
                modifier,
                title,
                scrollBehavior = scrollBehavior,
                onBackIconButtonClicked = onBackIconButtonClicked,
                topAppBarActions = topAppBarActions,
            )
        }

        // Small
        else -> Rn3ScaffoldImpl(
            modifier,
            TopAppBarDefaults.pinnedScrollBehavior(),
            content,
        ) { scrollBehavior ->
            Rn3SmallTopAppBar(
                modifier,
                title,
                scrollBehavior = scrollBehavior,
                onBackIconButtonClicked = onBackIconButtonClicked,
                topAppBarActions = topAppBarActions,
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
    topBarComponent: @Composable (scrollBehavior: TopAppBarScrollBehavior) -> Unit,
) {
    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = { topBarComponent(scrollBehavior) },
        contentWindowInsets = WindowInsets.displayCutout,
    ) {
        Column {
            content(it)
        }
    }
}

enum class TopAppBarStyle {
    SMALL, LARGE
}