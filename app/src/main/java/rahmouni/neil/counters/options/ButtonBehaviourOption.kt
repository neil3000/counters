package rahmouni.neil.counters.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.IncrementType

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
@Composable
fun ButtonBehaviourOption(
    incrementType: IncrementType,
    onSave: (IncrementType) -> Unit
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogIncrementType by rememberSaveable { mutableStateOf(incrementType) }
    var isDialogIncrementValueError by rememberSaveable { mutableStateOf(false) }

    val localHapticFeedback = LocalHapticFeedback.current

    ListItem(
        text = { androidx.compose.material.Text("Button behaviour") }, //TODO i18n
        secondaryText = {
            androidx.compose.material.Text(
                incrementType.title,
            )
        },
        modifier = Modifier
            .clickable(
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    dialogIncrementType = incrementType
                    isDialogIncrementValueError = false
                    openDialog = true
                }
            )
            .padding(8.dp)
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
                Text(text = "Button behaviour") //TODO i18n
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
                                    onClick = null // null recommended for accessibility with screenreaders
                                )
                                Text(
                                    text = it.title,
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
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        openDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}