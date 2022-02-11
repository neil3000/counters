package rahmouni.neil.counters.new_counter.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.OutlinedTextField
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.IncrementType

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
@Composable
fun DefaultIncrementOption(
    incrementType: IncrementType,
    incrementValue: Int,
    onSave: (IncrementType, Int) -> Unit
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogIncrementType by rememberSaveable { mutableStateOf(incrementType) }
    var dialogIncrementValue by rememberSaveable { mutableStateOf(incrementValue.toString()) }
    var isDialogIncrementValueError by rememberSaveable { mutableStateOf(false) }

    val localHapticFeedback = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun validateDialogIncrementValue(text: String): Boolean {
        isDialogIncrementValueError = text.toIntOrNull() == null || text.toInt() <= 0
        return !isDialogIncrementValueError
    }

    ListItem(
        text = { Text("Default increments") },
        secondaryText = {
            Text(
                incrementType.description.replace("%s", incrementValue.toString()),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = .7f)
            )
        },
        modifier = Modifier
            .clickable(
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    dialogIncrementType = incrementType
                    dialogIncrementValue = incrementValue.toString()
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
                Text(text = "Default increments") //TODO i18n
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
                            if (it.hasValue) {
                                OutlinedTextField(
                                    value = dialogIncrementValue,
                                    enabled = dialogIncrementType == it,
                                    onValueChange = { str ->
                                        isDialogIncrementValueError = false
                                        dialogIncrementValue = str
                                    },
                                    singleLine = true,
                                    isError = isDialogIncrementValueError,
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                    keyboardActions = KeyboardActions {
                                        if (validateDialogIncrementValue(dialogIncrementValue)) keyboardController?.hide()
                                    })
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

                        if (validateDialogIncrementValue(dialogIncrementValue)) {
                            onSave(dialogIncrementType, dialogIncrementValue.toInt())

                            openDialog = false
                        }
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