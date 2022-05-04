package rahmouni.neil.counters.utils.tiles.tile_color_selection

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class Size(val circlePadding: Dp, val circleSize: Dp, val selectedPadding: Dp, val iconSize: Dp) {
    SMALL(9.dp, 41.dp, 9.dp, 12.dp), BIG(12.dp, 54.dp, 12.dp, 16.dp)
}