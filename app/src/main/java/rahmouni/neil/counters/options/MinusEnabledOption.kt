package rahmouni.neil.counters.options

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.Switch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MinusEnabledOption(
    enabled: Boolean,
    inModal: Boolean = false,
    setEnabled: (Boolean) -> Unit
) {
    val localHapticFeedback = LocalHapticFeedback.current

    ListItem(
        text = {
            Text(stringResource(R.string.action_showMinusOnHomePage))
        },
        icon = if (!inModal) {
            { Icon(Icons.Outlined.Remove, null) }
        } else null,
        trailing = {
            Switch(
                checked = enabled,
                onCheckedChange = null,
            )
        },
        modifier = Modifier
            .toggleable(
                value = enabled
            ) {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                setEnabled(it)
            }
            .padding(if (inModal) 8.dp else 0.dp)
    )
}