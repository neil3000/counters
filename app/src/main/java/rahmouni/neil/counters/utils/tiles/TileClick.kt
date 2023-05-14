package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@Composable
fun TileClick(
    title: String,
    icon: ImageVector?,
    modifier: Modifier = Modifier,
    description: String? = null,
    onClick: () -> Unit
) {
    val haptics = LocalHapticFeedback.current

    ListItem(
        headlineContent = { Text(title) },
        supportingContent = if (description != null) {
            { Text(description) }
        } else null,
        leadingContent = if (icon != null) {
            { Icon(icon, null) }
        } else null,
        modifier = modifier.then(
            Modifier.clickable(
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                    onClick()
                }
            )
        )
    )
}