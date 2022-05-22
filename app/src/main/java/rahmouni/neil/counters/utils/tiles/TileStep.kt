package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.clickable
import androidx.compose.material.ContentAlpha
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.HighlightOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import rahmouni.neil.counters.R

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TileStep(
    title: String,
    description: String,
    done: Boolean,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val localHapticFeedback = LocalHapticFeedback.current

    ListItem(
        text = { Text(title) },
        secondaryText = { Text(description) },
        singleLineSecondaryText = true,
        icon = {
            if (done) {
                Icon(
                    Icons.Outlined.CheckCircle,
                    stringResource(R.string.text_done),
                    tint = MaterialTheme.colorScheme.primary
                )
            } else {
                Icon(
                    Icons.Outlined.HighlightOff,
                    stringResource(R.string.text_notDone),
                )
            }
        },
        trailing = {
            if (!done) {
                Icon(Icons.Outlined.ChevronRight, null)
            }
        },
        modifier = Modifier
            .alpha(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
            .clickable(
                enabled = !done && enabled,
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    onClick()
                }
            )
    )
}