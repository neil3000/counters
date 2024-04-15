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
    val communityMember: String?,
    val remoteConfigBooleanPath: String?
    ) {
    DEFAULT(null, true, null, null),
    SECONDARY(null, true, null, null),
    PRIMARY(null, true, null, null),
    TERTIARY(null, true, null, null),
    BEIGE(R.color.beige, false, null, null),
    ORANGE(R.color.orange, false, null, null),
    PURPLE_FAYE(R.color.purple_faye, false, "Faye", "i_271"),
    PURPLE_LAIZO(R.color.purple_laizo, false, "Laizo", "i_272"),
    PINK_CLARITY(R.color.pink_clarity, false, "Clarity", "i_273"),
    PINK(R.color.pink, false, null, null),
    BLUE(R.color.blue, false, null, null);

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