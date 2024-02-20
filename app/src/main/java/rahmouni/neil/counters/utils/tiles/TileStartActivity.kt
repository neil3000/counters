package rahmouni.neil.counters.utils.tiles

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun TileStartActivity(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    description: String? = null,
    activity: Class<*>,
    enabled: Boolean = true,
    extras: (Intent) -> (Intent) = { it }
) {
    val haptics = LocalHapticFeedback.current
    val context = LocalContext.current

    ListItem(
        headlineContent = { Text(title) },
        supportingContent = if (description != null) {
            { Text(description) }
        } else null,
        leadingContent = { Icon(icon, null) },
        modifier = modifier.then(
            Modifier
                .alpha(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
                .clickable(
                    enabled = enabled,
                    onClick = {
                        haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                        context.startActivity(
                            extras(Intent(context, activity))
                        )
                    }
                )
        )
    )
}