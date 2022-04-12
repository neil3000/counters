package rahmouni.neil.counters.utils

import androidx.compose.animation.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SelectableChip(
    text: String,
    selected: Boolean,
    onUnselected: () -> Unit,
    onSelected: () -> Unit,
) {
    val localHapticFeedback = LocalHapticFeedback.current

    val animatedColor = animateColorAsState(
        if (selected) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant
    )

    Surface(shape = RoundedCornerShape(8.dp), color = animatedColor.value) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .selectable(
                    selected = selected,
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        if (selected) onUnselected() else onSelected()
                    })
                .padding(horizontal = 12.dp)
        ) {
            Text(text, Modifier.padding(vertical = 8.dp))
            AnimatedVisibility(
                selected,
                enter = scaleIn() + expandHorizontally(),
                exit = scaleOut() + shrinkHorizontally()
            ) {
                Icon(
                    Icons.Outlined.Check,
                    null,
                    Modifier.padding(start = 6.dp)
                )
            }
        }
    }
}