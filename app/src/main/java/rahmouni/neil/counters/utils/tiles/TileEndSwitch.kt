package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@Composable
fun TileEndSwitch(
    checked: Boolean,
    enabled: Boolean = true,
    onChange: (Boolean) -> Unit,
    tile: @Composable (Modifier) -> Unit,
) {
    val localHapticFeedback = LocalHapticFeedback.current

    Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
        tile(Modifier.weight(1f))
        Box(
            Modifier
                .requiredHeight(48.dp)
                .requiredWidth(1.dp)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
        )
        Switch(
            checked = checked,
            onCheckedChange = {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                onChange(!checked)
            },
            Modifier.padding(horizontal = 16.dp),
            enabled = enabled,
        )
    }
}