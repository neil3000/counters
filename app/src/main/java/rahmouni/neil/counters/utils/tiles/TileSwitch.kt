package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
    onChange: (Boolean) -> Unit
) {
    val localHapticFeedback = LocalHapticFeedback.current

    ListItem(
        text = { Text(title) },
        icon = { Icon(icon, null) },
        trailing = { Switch(checked = checked, onCheckedChange = null) },
        modifier = Modifier
            .toggleable(
                value = checked,
                role = Role.Switch,
                onValueChange = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    onChange(it)
                }
            )
    )
}