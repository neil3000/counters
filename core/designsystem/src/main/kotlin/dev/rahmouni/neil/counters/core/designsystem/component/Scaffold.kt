package dev.rahmouni.neil.counters.core.designsystem.component

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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.window.core.layout.WindowHeightSizeClass.Companion.COMPACT
import dev.rahmouni.neil.counters.core.designsystem.component.feedback.FeedbackBottomSheet
import dev.rahmouni.neil.counters.core.designsystem.component.topAppBar.Rn3LargeTopAppBar
import dev.rahmouni.neil.counters.core.designsystem.component.topAppBar.Rn3SmallTopAppBar
import dev.rahmouni.neil.counters.core.feedback.FeedbackHelper
import dev.rahmouni.neil.counters.core.feedback.LocalFeedbackHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Rn3Scaffold(
    modifier: Modifier = Modifier,
    title: String,
    onBackIconButtonClicked: (() -> Unit)? = null,
    feedbackHelper: FeedbackHelper,
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
            feedbackHelper,
        ) { scrollBehavior, showFeedbackModal ->
            Rn3LargeTopAppBar(
                modifier,
                title,
                scrollBehavior = scrollBehavior,
                onBackIconButtonClicked = onBackIconButtonClicked,
                onFeedbackIconButtonClicked = showFeedbackModal
            )
        }

        // Small
        else -> Rn3ScaffoldImpl(
            modifier,
            TopAppBarDefaults.pinnedScrollBehavior(),
            content,
            feedbackHelper,
        ) { scrollBehavior, showFeedbackModal ->
            Rn3SmallTopAppBar(
                modifier,
                title,
                scrollBehavior = scrollBehavior,
                onBackIconButtonClicked = onBackIconButtonClicked,
                onFeedbackIconButtonClicked = showFeedbackModal
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun Rn3ScaffoldImpl(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable (PaddingValues) -> Unit,
    feedbackHelper: FeedbackHelper,
    topBarComponent: @Composable (scrollBehavior: TopAppBarScrollBehavior, openFeedbackModal: () -> Unit) -> Unit,
) {
    var showFeedbackModal by remember { mutableStateOf(false) }

    CompositionLocalProvider(LocalFeedbackHelper provides feedbackHelper) { //TODO
        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = { topBarComponent(scrollBehavior) { showFeedbackModal = true } },
            contentWindowInsets = WindowInsets.displayCutout,
        ) {
            Column {
                content(it)

                FeedbackBottomSheet(showFeedbackModal) { showFeedbackModal = false }
            }
        }
    }
}

enum class TopAppBarStyle {
    SMALL, LARGE
}