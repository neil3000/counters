package dev.rahmouni.neil.counters.core.designsystem.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.Modifier

fun LazyListScope.systemBarSpacer() {
    item { Spacer(modifier = Modifier.windowInsetsBottomHeight(WindowInsets.systemBars)) }
}