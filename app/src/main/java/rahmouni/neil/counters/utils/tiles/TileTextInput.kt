package rahmouni.neil.counters.utils.tiles

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.AutoFocusOutlinedTextField

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun TileTextInput(
    title: String,
    dialogTitle: String? = null,
    icon: ImageVector,
    confirmString: String? = null,
    value: String,
    validateInput: (String) -> Boolean,
    onSave: (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localHapticFeedback = LocalHapticFeedback.current

    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogValue by rememberSaveable { mutableStateOf(value) }
    var isError by rememberSaveable { mutableStateOf(false) }

    fun closeDialog() {
        keyboardController?.hide()
        openDialog = false
        dialogValue = value
        isError = false
    }

    fun confirm() {
        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

        if (validateInput(dialogValue)) {
            onSave(dialogValue)

            keyboardController?.hide()
            openDialog = false
        } else {
            isError = true
        }
    }

    ListItem(
        text = { androidx.compose.material.Text(title) },
        secondaryText = { androidx.compose.material.Text(value) },
        singleLineSecondaryText = true,
        icon = { Icon(icon, null) },
        modifier = Modifier
            .clickable(
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    openDialog = true
                }
            )
    )

    if (openDialog) {
        AlertDialog(
            onDismissRequest = {
                closeDialog()
            },
            title = {
                Text(dialogTitle ?: title)
            },
            icon = { Icon(icon, null) },
            text = {
                AutoFocusOutlinedTextField(
                    value = dialogValue,
                    isError = isError,
                    keyboardActions = KeyboardActions { confirm() }
                ) {
                    isError = false
                    dialogValue = it
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirm()
                    }
                ) {
                    Text(confirmString ?: stringResource(R.string.action_save_short))
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