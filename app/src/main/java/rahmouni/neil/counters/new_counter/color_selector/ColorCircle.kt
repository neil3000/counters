package rahmouni.neil.counters.new_counter.color_selector

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.ui.theme.CountersTheme

const val RADIUS = 24
const val SPACING = 4
const val STROKE = 4

@Composable
fun ColorCircle(counterStyle: CounterStyle, selected: Boolean, onSelected: () -> Unit) {
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
            color = counterStyle.getBackGroundColor(),
            shape = CircleShape
        ) {}
    }
}

@Preview(showBackground = true)
@Composable
fun ColorCirclePreview() {
    CountersTheme {
        ColorCircle(CounterStyle.DEFAULT, false) {}
    }
}

@Preview(showBackground = true)
@Composable
fun ColorCirclePreviewSelected() {
    CountersTheme {
        ColorCircle(CounterStyle.DEFAULT, true) {}
    }
}