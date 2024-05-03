package dev.rahmouni.neil.counters.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme

private const val DEFAULT_HORIZONTAL_PADDING__DP = 32
private const val DEFAULT_ARRANGEMENT_HORIZONTAL_SPACING__DP = 16

@Composable
fun Rn3LazyRowWithPadding(
    modifier: Modifier = Modifier,
    horizontalPadding: Dp = DEFAULT_HORIZONTAL_PADDING__DP.dp,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(
        DEFAULT_ARRANGEMENT_HORIZONTAL_SPACING__DP.dp,
    ),
    verticalAlignment: Alignment.Vertical = Alignment.Top,
    content: LazyListScope.() -> Unit,
) {
    LazyRow(
        modifier,
        horizontalArrangement = horizontalArrangement,
        verticalAlignment = verticalAlignment,
    ) {
        rn3Spacer(horizontalPadding)
        content()
        rn3Spacer(horizontalPadding)
    }
}

private fun LazyListScope.rn3Spacer(horizontalPadding: Dp) {
    item {
        Spacer(modifier = Modifier.size(horizontalPadding, 0.dp))
    }
}

@Composable
@Rn3PreviewComponentDefault
private fun Default() {
    Rn3Theme {
        Rn3LazyRowWithPadding {
            repeat(10) {
                item {
                    Rn3Switch(checked = it % 2 == 0, contentDescription = null) {}
                }
            }
        }
    }
}