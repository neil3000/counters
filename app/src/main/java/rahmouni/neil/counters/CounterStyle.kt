package rahmouni.neil.counters

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.google.firebase.remoteconfig.FirebaseRemoteConfig

// PrimaryContainer:
// -> Light = Primary90 / A1-100
// -> Dark  = Primary30 / A1-700
enum class CounterStyle(
    private val backgroundColor: Int?,
    val isDynamic: Boolean,
    val remoteConfigBooleanPath: String?
) {
    DEFAULT(null, true, null),
    SECONDARY(null, true, null),
    PRIMARY(null, true, null),
    TERTIARY(null, true, null),
    BEIGE(R.color.beige, false, null),
    ORANGE(R.color.orange, false, null),
    PURPLE_FAYE(R.color.purple_faye, false, "271"),
    PINK(R.color.pink, false, null),
    BLUE(R.color.blue, false, null);

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

    companion object {
        fun getAvailableValues(dynamicColor: Boolean): List<CounterStyle> {
            val remoteConfig = FirebaseRemoteConfig.getInstance()

            return CounterStyle.values().filter {
                (if (it.remoteConfigBooleanPath.isNullOrEmpty()) true else remoteConfig.getBoolean(
                    it.remoteConfigBooleanPath
                ))
                        && it.isDynamic == dynamicColor
            }
        }
    }
}