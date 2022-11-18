package rahmouni.neil.counters.utils

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import rahmouni.neil.counters.prefs

@Composable
fun Switch(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    thumbContent: @Composable (() -> Unit)? = null,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null
) {
    val iconSwitchesEnabled = prefs.preferences.booleanLiveData("ICON_SWITCHES", false)
        .observeAsState(false)

    if (interactionSource != null) {
        androidx.compose.material3.Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            thumbContent = thumbContent ?: if (iconSwitchesEnabled.value && checked) {
                { Icon(Icons.Outlined.Check, null, Modifier.scale(.75f)) }
            } else null,
            enabled = enabled,
            interactionSource = interactionSource
        )
    } else {
        androidx.compose.material3.Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = modifier,
            thumbContent = thumbContent ?: if (iconSwitchesEnabled.value && checked) {
                { Icon(Icons.Outlined.Check, null, Modifier.scale(.75f)) }
            } else null,
            enabled = enabled
        )
    }
}