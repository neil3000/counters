package rahmouni.neil.counters.utils

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.android.awaitFrame

@Composable
fun AutoFocusOutlinedTextField(
    value: String,
    isError: Boolean,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    enabled: Boolean = true,
    onValueChange: (value: String) -> Unit
) {
    var dialogNameStr by rememberSaveable { mutableStateOf(value) }
    var dialogName by remember { mutableStateOf(TextFieldValue()) }
    val focusRequester = remember { FocusRequester() }

    OutlinedTextField(
        value = dialogName,
        onValueChange = { str ->
            dialogNameStr = str.text
            dialogName = str

            onValueChange(str.text)
        },
        singleLine = true,
        modifier = Modifier.focusRequester(focusRequester),
        isError = isError,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        enabled = enabled
    )


    // enabled in LaunchedEffect so it requests focus if enabled after being disabled
    LaunchedEffect(enabled) {
        // autofocus the TextField. Two awaitFrame are required bc one only works sometimes
        awaitFrame()
        awaitFrame()
        focusRequester.requestFocus()
    }
    LaunchedEffect(Unit) {
        // dialogName cannot be a rememberSaveable so it is a remember.
        // To save its value, we use dialogNameStr and update dialogName when the component is refreshed.
        dialogName = TextFieldValue(
            text = dialogNameStr,
            selection = TextRange(dialogNameStr.length, dialogNameStr.length)
        )
    }
}