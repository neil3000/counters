package dev.rahmouni.neil.counters.core.designsystem.roundedCorners

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun Rn3RoundedCornersSurfaceGroup(content: SurfaceGroupScope.() -> Unit) {
    val localScope = SurfaceGroupScope()
    localScope.content()

    Column(verticalArrangement = spacedBy(3.dp)) {
        with(localScope.items) {

            var index = 0
            var invisibleCount = 0

            forEach { item ->
                var shape =
                    Rn3RoundedCorners(Rn3RoundedCornersSurfaceGroupDefaults.roundedCornerInternal)

                if (index == 0) shape += Rn3RoundedCorners(top = Rn3RoundedCornersSurfaceGroupDefaults.roundedCornerExternal - Rn3RoundedCornersSurfaceGroupDefaults.roundedCornerInternal)
                if (index == size - invisibleCount - 1) shape += Rn3RoundedCorners(bottom = Rn3RoundedCornersSurfaceGroupDefaults.roundedCornerExternal - Rn3RoundedCornersSurfaceGroupDefaults.roundedCornerInternal)

                AnimatedVisibility(visible = item.visible) {
                    item.content(shape)
                }

                if (item.visible) index++ else invisibleCount++
            }
        }
    }
}

internal data class SurfaceGroupItem(
    val content: @Composable (Rn3RoundedCorners) -> Unit,
    val visible: Boolean,
)

class SurfaceGroupScope {
    internal val items = mutableListOf<SurfaceGroupItem>()

    fun item(visible: Boolean = true, content: @Composable (Rn3RoundedCorners) -> Unit) {
        items.add(SurfaceGroupItem(content, visible))
    }
}

object Rn3RoundedCornersSurfaceGroupDefaults {
    val roundedCornerInternal = 6.dp
    val roundedCornerExternal = 24.dp
}