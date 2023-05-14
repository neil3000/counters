package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.clickable
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.HighlightOff
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import rahmouni.neil.counters.R

@Composable
fun TileStep(
    title: String,
    description: String,
    done: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val haptics = LocalHapticFeedback.current

    ListItem(
        headlineContent = { Text(title) },
        supportingContent = { Text(description) },
        leadingContent = {
            if (done) {
                // Done
                Icon(
                    Icons.Outlined.CheckCircle,
                    stringResource(R.string.tileStep_icon_done_contentDescription),
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                // NotDone
                Icon(
                    Icons.Outlined.HighlightOff,
                    stringResource(R.string.tileStep_icon_notDone_contentDescription),
                )
            }
        },
        trailingContent = {
            if (!done) {
                Icon(Icons.Outlined.ChevronRight, null)
            }
        },
        modifier = Modifier
            .alpha(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
            .clickable(
                enabled = !done && enabled,
                onClick = {
                    haptics.performHapticFeedback(HapticFeedbackType.LongPress)

                    onClick()
                }
            )
    )
}