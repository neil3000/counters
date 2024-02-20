package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.ContentAlpha
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role

@Composable
fun TileSwitch(
    title: String,
    description: String? = null,
    icon: ImageVector,
    checked: Boolean,
    enabled: Boolean = true,
    onChange: (Boolean) -> Unit
) {
    val haptics = LocalHapticFeedback.current

    val interactionSource = remember { MutableInteractionSource() }

    ListItem(
        headlineContent = { Text(title) },
        supportingContent = if (description != null) {
            { Text(description) }
        } else null,
        leadingContent = {
            Icon(icon, null)
        },
        trailingContent = {
            rahmouni.neil.counters.utils.Switch(
                checked = checked,
                onCheckedChange = null,
                interactionSource = interactionSource,
            )
        },
        modifier = Modifier
            .alpha(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
            .toggleable(
                interactionSource = interactionSource,
                value = checked,
                indication = LocalIndication.current,
                role = Role.Switch,
                enabled = enabled,
            ) {
                haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                onChange(it)
            }
    )
}