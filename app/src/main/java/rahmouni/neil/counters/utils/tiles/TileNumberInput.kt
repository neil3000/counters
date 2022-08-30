package rahmouni.neil.counters.utils.tiles

/*
@OptIn(ExperimentalMaterialApi::class, ExperimentalComposeUiApi::class)
@Composable
fun TileNumberInput(
    title: String,
    dialogTitle: String? = null,
    icon: ImageVector,
    confirmString: String? = null,
    value: Int,
    format: Int? = null, //The R.id value of a string with %d in it
    validateInput: (Int) -> Boolean = { true },
    enabled: Boolean = true,
    onSave: (Int) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val localHapticFeedback = LocalHapticFeedback.current

    var openDialog by rememberSaveable { mutableStateOf(false) }
    var dialogValue by rememberSaveable { mutableStateOf(value.toString()) }
    var isError by rememberSaveable { mutableStateOf(false) }

    fun closeDialog() {
        keyboardController?.hide()
        openDialog = false
        dialogValue = value.toString()
        isError = false
    }

    fun confirm() {
        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

        if (dialogValue.toIntOrNull() != null && validateInput(dialogValue.toInt())) {
            onSave(dialogValue.toInt())

            keyboardController?.hide()
            openDialog = false
        } else {
            isError = true
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
                if (format != null) stringResource(
                    format,
                    value
                ) else value.toString(),
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
                Text(dialogTitle ?: title)
            },
            icon = { Icon(icon, null) },
            text = {
                AutoFocusOutlinedTextField(
                    value = dialogValue,
                    isError = isError,
                    keyboardActions = KeyboardActions { confirm() },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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
*/