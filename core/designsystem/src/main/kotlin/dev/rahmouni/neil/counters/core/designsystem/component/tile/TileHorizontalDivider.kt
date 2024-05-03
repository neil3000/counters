package dev.rahmouni.neil.counters.core.designsystem.component.tile

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewComponentDefault
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme

@Composable
fun Rn3TileHorizontalDivider(modifier: Modifier = Modifier, color: Color = DividerDefaults.color) {
    HorizontalDivider(modifier.padding(horizontal = 16.dp, vertical = 8.dp), color = color)
}

@Rn3PreviewComponentDefault
@Composable
private fun Default() {
    Rn3Theme {
        Surface {
            Rn3TileHorizontalDivider()
        }
    }
}