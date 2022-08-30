package rahmouni.neil.counters.value_types

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@Composable
fun TaskValuePicker(
    value: String,
    onChange: (String) -> Unit
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        listOf("0", "1").forEach {
            val animatedColor = animateColorAsState(
                if (value == it) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface
            )
            val animatedCorners =
                animateDpAsState(if (value == it) 32.dp else 16.dp)

            Surface(
                color = animatedColor.value,
                tonalElevation = -LocalAbsoluteTonalElevation.current,
                shape = RoundedCornerShape(animatedCorners.value),
                modifier = Modifier.weight(1f).padding(4.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .selectable(
                            selected = value == it,
                            onClick = {
                                localHapticFeedback.performHapticFeedback(
                                    HapticFeedbackType.LongPress
                                )

                                onChange(it)
                            },
                            role = Role.RadioButton
                        )
                        .padding(12.dp)
                ) {
                    Icon(
                        if (it == "1") Icons.Outlined.Check else Icons.Outlined.Close,
                        null,
                        Modifier
                            .padding(bottom = 4.dp)
                            .width(32.dp)
                    )
                    Text(
                        text = ValueType.TASK.formatAsString(it.toInt(), context),
                        color = contentColorFor(animatedColor.value),
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            }
        }
    }
}