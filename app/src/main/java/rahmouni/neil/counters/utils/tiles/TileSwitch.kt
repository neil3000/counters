package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.selection.toggleable
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
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import rahmouni.neil.counters.utils.Switch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TileSwitch(
    title: String,
    icon: ImageVector,
    checked: Boolean,
    enabled: Boolean = true,
    onChange: (Boolean) -> Unit
) {
    val localHapticFeedback = LocalHapticFeedback.current

    ListItem(
        text = {
            Text(
                title,
                Modifier.alpha(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
            )
        },
        icon = { Icon(icon, null) },
        trailing = { Switch(checked = checked, onCheckedChange = null, enabled = enabled) },
        modifier = Modifier
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