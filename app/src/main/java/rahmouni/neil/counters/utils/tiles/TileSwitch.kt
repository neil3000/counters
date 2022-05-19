package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TileSwitch(
    title: String,
    description: String? = null,
    icon: ImageVector,
    checked: Boolean,
    enabled: Boolean = true,
    onChange: (Boolean) -> Unit
) {
    val localHapticFeedback = LocalHapticFeedback.current
    //val remoteConfig = FirebaseRemoteConfig.getInstance()

    ListItem(
        text = { Text(title) },
        secondaryText = if (description != null) {
            { Text(description) }
        } else null,
        singleLineSecondaryText = true,
        icon = { Icon(icon, null) },
        trailing = {
            Switch(checked = checked, onCheckedChange = null)
        },
        modifier = Modifier
            .alpha(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
            .toggleable(
                value = checked,
                role = Role.Switch,
                enabled = enabled,
            ) {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                onChange(it)
            }
    )
}