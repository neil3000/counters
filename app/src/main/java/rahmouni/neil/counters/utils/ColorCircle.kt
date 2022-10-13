package rahmouni.neil.counters.utils

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

const val RADIUS = 24
const val SPACING = 4
const val STROKE = 4

@Composable
fun ColorCircle(color: Color, selected: Boolean, onSelected: () -> Unit) {
    val localHapticFeedback = LocalHapticFeedback.current

    IconButton(onClick = {
        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        onSelected()
    }) {
        if (selected) {
            Surface(
                modifier = Modifier.size(RADIUS.dp + SPACING.dp + STROKE.dp),
                color = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {}
        }
        Surface(
            modifier = Modifier.size(RADIUS.dp + SPACING.dp),
            color = MaterialTheme.colorScheme.background,
            shape = CircleShape
        ) {}
        Surface(
            modifier = Modifier.size(RADIUS.dp),
            color = color,
            shape = CircleShape
        ) {}
    }
}