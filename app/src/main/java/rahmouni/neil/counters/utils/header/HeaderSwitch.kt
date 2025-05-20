package rahmouni.neil.counters.utils.header

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun HeaderSwitch(
    title: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit
) {
    val localHapticFeedback = LocalHapticFeedback.current

    val animatedColor = animateColorAsState(
        if (checked) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
    )
    val interactionSource = remember { MutableInteractionSource() }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        Surface(
            color = animatedColor.value,
            modifier = Modifier
                .fillMaxWidth()
                .alpha(if (enabled) 1f else .7f)
                .toggleable(
                    interactionSource = interactionSource,
                    value = checked,
                    indication = LocalIndication.current,
                    role = Role.Switch,
                    enabled = enabled,
                ) {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    onCheckedChange(it)
                },
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(24.dp)
                        .testTag("HEADER_TITLE")
                )
                rahmouni.neil.counters.utils.Switch(
                    checked = checked,
                    onCheckedChange = null,
                    Modifier.padding(16.dp),
                    interactionSource = interactionSource,
                    enabled = enabled
                )
            }
        }
    }
}