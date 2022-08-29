package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TileClick(
    title: String,
    icon: ImageVector?,
    modifier: Modifier = Modifier,
    description: String? = null,
    onClick: () -> Unit
) {
    val localHapticFeedback = LocalHapticFeedback.current

    ListItem(
        text = { Text(title) },
        secondaryText = if (description != null) {
            { Text(description) }
        } else null,
        singleLineSecondaryText = true,
        icon = if (icon != null) {
            { Icon(icon, null) }
        } else null,
        modifier = modifier.then(
            Modifier.clickable(
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    onClick()
                }
            )
        )
    )
}