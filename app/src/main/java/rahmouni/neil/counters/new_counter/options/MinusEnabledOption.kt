package rahmouni.neil.counters.new_counter.options

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.utils.Switch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MinusEnabledOption(enabled: Boolean, setEnabled: (Boolean) -> Unit) {
    val localHapticFeedback = LocalHapticFeedback.current

    ListItem(
        text = {
            Text(
                "Show minus on home page",
            )
        }, //TODO i18n
        trailing = {
            Switch(
                checked = enabled,
                onCheckedChange = null,
            )
        },
        modifier = Modifier
            .toggleable(
                value = enabled,
                onValueChange = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    setEnabled(it)
                }
            )
            .padding(8.dp)
    )
}