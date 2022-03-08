package rahmouni.neil.counters.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.IncrementType
import rahmouni.neil.counters.R

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
@Composable
fun ButtonBehaviourOption(
    incrementType: IncrementType,
    inModal: Boolean = false,
    onSave: (IncrementType) -> Unit,
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogIncrementType by rememberSaveable { mutableStateOf(incrementType) }
    var isDialogIncrementValueError by rememberSaveable { mutableStateOf(false) }

    val localHapticFeedback = LocalHapticFeedback.current

    ListItem(
        text = { androidx.compose.material.Text(stringResource(R.string.text_buttonBehavior)) },
        secondaryText = {
            androidx.compose.material.Text(
                stringResource(incrementType.title),
            )
        },
        icon = if (!inModal) {
            { Icon(Icons.Outlined.TouchApp, null) }
        } else null,
        modifier = Modifier
            .clickable(
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    dialogIncrementType = incrementType
                    isDialogIncrementValueError = false
                    openDialog = true
                }
            )
            .padding(if (inModal) 8.dp else 0.dp)
    )

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog = false
            },
            title = {
                Text(stringResource(R.string.text_buttonBehavior))
            },
            text = {
                Column {
                    IncrementType.values().forEach {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = dialogIncrementType == it,
                                    onClick = {
                                        localHapticFeedback.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )

                                        dialogIncrementType = it
                                    },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),

                            ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.height(56.dp)
                            ) {
                                RadioButton(
                                    selected = dialogIncrementType == it,
                                    onClick = null
                                )
                                Text(
                                    text = stringResource(it.title),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !isDialogIncrementValueError,
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        onSave(dialogIncrementType)

                        openDialog = false
                    }
                ) {
                    Text(stringResource(R.string.action_save_short))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        openDialog = false
                    }
                ) {
                    Text(stringResource(R.string.action_cancel_short))
                }
            }
        )
    }
}