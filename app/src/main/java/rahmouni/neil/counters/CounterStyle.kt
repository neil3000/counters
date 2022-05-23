package rahmouni.neil.counters

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource

enum class CounterStyle(private val backgroundColor: Int?, val isDynamic: Boolean) {
    DEFAULT(null, true),
    SECONDARY(null, true),
    PRIMARY(null, true),
    TERTIARY(null, true),
    BEIGE(R.color.beige, false),
    ORANGE(R.color.orange, false),
    PINK(R.color.pink, false),
    BLUE(R.color.blue, false);

    @Composable
    fun getBackGroundColor(): Color {
        return when (this) {
            DEFAULT -> MaterialTheme.colorScheme.surfaceVariant
            SECONDARY -> MaterialTheme.colorScheme.secondaryContainer
            PRIMARY -> MaterialTheme.colorScheme.primaryContainer
            TERTIARY -> MaterialTheme.colorScheme.tertiaryContainer
            else -> colorResource(id = backgroundColor!!)
        }
    }
}