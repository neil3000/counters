package rahmouni.neil.counters.options

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Title
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class,
    androidx.compose.ui.ExperimentalComposeUiApi::class
)
@Composable
fun NameOption(
    name: String,
    inModal: Boolean = false,
    onSave: (String) -> Unit
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogName by rememberSaveable { mutableStateOf("Counter") }
    var isDialogNameError by rememberSaveable { mutableStateOf(false) }

    val localHapticFeedback = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current

    fun validateDialogName(text: String): Boolean {
        isDialogNameError = text.count() < 1
        return !isDialogNameError
    }

    fun confirm() {
        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

        if (validateDialogName(dialogName)) {
            keyboardController?.hide()

            onSave(dialogName)

            openDialog = false
        }
    }

    ListItem(
        text = { Text("Name") }, //TODO i18n
        secondaryText = {
            androidx.compose.material.Text(name)
        },
        icon = if (!inModal) {
            { Icon(Icons.Outlined.Title, null) }
        } else null,
        modifier = Modifier
            .clickable(
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    dialogName = name
                    isDialogNameError = false
                    openDialog = true
                }
            )
            .padding(if (inModal) 8.dp else 0.dp)
    )
    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                openDialog = false
            },
            title = {
                Text("Edit name") //TODO i18n
            },
            icon = { Icon(Icons.Outlined.Title, null) },
            text = {
                Column {
                    OutlinedTextField(
                        value = dialogName,
                        onValueChange = { str ->
                            isDialogNameError = false
                            dialogName = str
                        },
                        singleLine = true,
                        isError = isDialogNameError,
                        keyboardActions = KeyboardActions { confirm() })
                }
            },
            confirmButton = {
                TextButton(
                    enabled = !isDialogNameError,
                    onClick = { confirm() }
                ) {
                    Text("Save") //TODO i18n
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        openDialog = false
                    }
                ) {
                    Text("Cancel") //TODO i18n
                }
            }
        )
    }
}