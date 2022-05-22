package rahmouni.neil.counters.utils.tiles

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TileSwitchStartActivity(
    title: String,
    description: String? = null,
    icon: ImageVector,
    activity: Class<*>,
    checked: Boolean,
    tileEnabled: Boolean = true,
    switchEnabled: Boolean = true,
    extras: (Intent) -> (Intent) = { it },
    onChange: (Boolean) -> Unit
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    Row(Modifier.fillMaxHeight(), verticalAlignment = Alignment.CenterVertically) {
        ListItem(
            text = { Text(title) },
            secondaryText = if (description != null) {
                { Text(description) }
            } else null,
            singleLineSecondaryText = true,
            icon = { Icon(icon, null) },
            modifier = Modifier
                .alpha(if (tileEnabled) ContentAlpha.high else ContentAlpha.disabled)
                .clickable(enabled = tileEnabled) {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    context.startActivity(
                        extras(Intent(context, activity))
                    )
                }
                .weight(1f)
        )
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
            enabled = switchEnabled,
        )
    }
}