package rahmouni.neil.counters.utils.tiles

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TileStartActivity(
    title: String,
    description: String? = null,
    icon: ImageVector,
    activity: Class<*>,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    extras: (Intent) -> (Intent) = { it }
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    ListItem(
        text = { Text(title) },
        secondaryText = if (description != null) {
            { Text(description) }
        } else null,
        icon = { Icon(icon, null) },
        modifier = modifier.then(
            Modifier
                .alpha(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
                .clickable(
                    enabled = enabled,
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        context.startActivity(
                            extras(Intent(context, activity))
                        )
                    }
                )
        )
    )
}