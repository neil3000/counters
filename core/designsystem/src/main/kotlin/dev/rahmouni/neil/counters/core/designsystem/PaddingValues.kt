package dev.rahmouni.neil.counters.core.designsystem

import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp

data class Rn3PaddingValues(val start: Dp, val end: Dp, val top: Dp, val bottom: Dp) {
    constructor(horizontal: Dp, vertical: Dp) : this(
        start = horizontal,
        end = horizontal,
        top = vertical,
        bottom = vertical,
    )
}

fun Modifier.padding(paddingValues: Rn3PaddingValues) = padding(
    start = paddingValues.start,
    end = paddingValues.end,
    top = paddingValues.top,
    bottom = paddingValues.bottom,
)