package rahmouni.neil.counters.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun SelectableChip(
    text: String,
    selected: Boolean,
    onUnselected: () -> Unit,
    onSelected: () -> Unit,
) {
    val localHapticFeedback = LocalHapticFeedback.current

    Surface(shape = RoundedCornerShape(8.dp),){
        Surface(
            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.selectable(selected = selected
            ) {
            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

            if (selected) onUnselected() else onSelected()
        }) {
            Text(text, Modifier.padding(horizontal = 12.dp, vertical = 8.dp))
        }
    }
}