package rahmouni.neil.counters.options

import androidx.compose.foundation.clickable
import androidx.compose.material.ContentAlpha
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import rahmouni.neil.counters.R
import rahmouni.neil.counters.value_types.ValueType

@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun ValueOption(
    title: String,
    secondaryFormatter: Int? = null,
    icon: ImageVector,
    dialogTitle: String,
    valueType: ValueType,
    value: Int,
    enabled: Boolean = true,
    onSave: (Int) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localHapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogValue by rememberSaveable { mutableStateOf(value.toString()) }

    fun closeDialog() {
        keyboardController?.hide()
        openDialog = false
        dialogValue = value.toString()
    }

    fun confirm() {
        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

        if (valueType.isValueValid(dialogValue)) {
            onSave(dialogValue.toInt())

            keyboardController?.hide()
            openDialog = false
        }
    }

    ListItem(
        text = {
            androidx.compose.material.Text(
                title,
                Modifier.alpha(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
            )
        },
        secondaryText = {
            androidx.compose.material.Text(
                if (secondaryFormatter != null) stringResource(
                    secondaryFormatter,
                    valueType.formatAsString(value, context)
                ) else valueType.formatAsString(value, context),
                Modifier.alpha(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
            )
        },
        singleLineSecondaryText = true,
        icon = {
            Icon(
                icon,
                null,
                Modifier.alpha(if (enabled) ContentAlpha.high else ContentAlpha.disabled)
            )
        },
        modifier = Modifier
            .clickable(
                enabled = enabled,
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
                Text(dialogTitle)
            },
            icon = { Icon(icon, null) },
            text = {
                valueType.picker(dialogValue) {
                    dialogValue = it
                }
            },
            confirmButton = {
                TextButton(
                    enabled = valueType.isValueValid(dialogValue),
                    onClick = {
                        confirm()
                    }
                ) {
                    Text(stringResource(R.string.valueOption_alertDialog_confirmButton_text))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        closeDialog()
                    }
                ) {
                    Text(stringResource(R.string.valueOption_alertDialog_dismissButton_text))
                }
            }
        )
    }
}