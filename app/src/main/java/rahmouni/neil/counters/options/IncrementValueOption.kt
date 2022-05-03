package rahmouni.neil.counters.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlusOne
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.IncrementType
import rahmouni.neil.counters.IncrementValueType
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.AutoFocusOutlinedTextField

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
@Composable
fun IncrementValueOption(
    incrementType: IncrementType,
    incrementValueType: IncrementValueType,
    incrementValue: Int,
    hasMinus: Boolean,
    inModal: Boolean = false,
    onSave: (IncrementValueType, Int) -> Unit
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogIncrementValueType by rememberSaveable { mutableStateOf(IncrementValueType.VALUE) }
    var dialogIncrementValue by rememberSaveable { mutableStateOf("1") }
    var isDialogIncrementValueError by rememberSaveable { mutableStateOf(false) }

    val localHapticFeedback = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun validateDialogIncrementValue(text: String): Boolean {
        isDialogIncrementValueError = text.toIntOrNull() == null || text.toInt() <= 0
        return !isDialogIncrementValueError
    }

    val titleStr = stringResource(
        if (incrementType == IncrementType.VALUE) {
            if (hasMinus) R.string.action_increaseDecreaseBy else R.string.action_increaseBy
        } else {
            R.string.text_defaultEntryValue
        }
    )

    fun closeDialog() {
        keyboardController?.hide()
        openDialog = false
    }

    fun confirm() {
        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

        if (validateDialogIncrementValue(dialogIncrementValue)) {
            onSave(dialogIncrementValueType, dialogIncrementValue.toInt())

            closeDialog()
        }
    }

    ListItem(
        text = { androidx.compose.material.Text(titleStr) },
        secondaryText = {
            androidx.compose.material.Text(
                if (incrementValueType.description == null)
                    incrementValue.toString()
                else stringResource(
                    incrementValueType.description
                )
            )
        },
        icon = if (!inModal) {
            { Icon(Icons.Outlined.PlusOne, null) }
        } else null,
        modifier = Modifier
            .clickable(
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    dialogIncrementValueType = incrementValueType
                    dialogIncrementValue = incrementValue.toString()
                    isDialogIncrementValueError = false
                    openDialog = true
                }
            )
            .padding(if (inModal) 8.dp else 0.dp)
    )
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                closeDialog()
            },
            title = {
                Text(titleStr)
            },
            text = {
                Column {
                    IncrementValueType.values().forEach {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .selectable(
                                    selected = dialogIncrementValueType == it,
                                    onClick = {
                                        localHapticFeedback.performHapticFeedback(
                                            HapticFeedbackType.LongPress
                                        )

                                        dialogIncrementValueType = it
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
                                    selected = dialogIncrementValueType == it,
                                    onClick = null
                                )
                                Text(
                                    text = stringResource(it.title),
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.padding(start = 16.dp)
                                )
                            }
                            if (it.hasValue) {
                                AutoFocusOutlinedTextField(
                                    value = dialogIncrementValue,
                                    enabled = dialogIncrementValueType == it,
                                    onValueChange = { str ->
                                        isDialogIncrementValueError = false
                                        dialogIncrementValue = str
                                    },
                                    isError = isDialogIncrementValueError,
                                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                    keyboardActions = KeyboardActions { confirm() })
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !isDialogIncrementValueError,
                    onClick = { confirm() }
                ) {
                    Text(stringResource(R.string.action_save_short))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        closeDialog()
                    }
                ) {
                    Text(stringResource(R.string.action_cancel_short))
                }
            }
        )
    }
}