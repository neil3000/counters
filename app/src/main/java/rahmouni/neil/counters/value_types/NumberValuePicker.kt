package rahmouni.neil.counters.value_types

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun NumberValuePicker(
    value: String,
    onChange: (String) -> Unit
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(horizontalArrangement = Arrangement.SpaceEvenly) {
        FilledTonalIconButton(
            onClick = {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                if (ValueType.NUMBER.isValueValid(value)) {
                    onChange((value.toInt() - 1).toString())
                }
            },
            enabled = ValueType.NUMBER.isValueValid(value)
        ) {
            Icon(
                Icons.Outlined.Remove,
                stringResource(R.string.action_decreaseValue)
            )
        }
        TextField(
            value = value,
            onValueChange = { str ->
                onChange(str)
            },
            singleLine = true,
            modifier = Modifier.widthIn(50.dp, 150.dp).padding(horizontal = 24.dp),
            isError = !ValueType.NUMBER.isValueValid(value),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            keyboardActions = KeyboardActions { keyboardController?.hide() })
        FilledTonalIconButton(
            onClick = {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                if (ValueType.NUMBER.isValueValid(value)) {
                    onChange((value.toInt() + 1).toString())
                }
            },
            enabled = ValueType.NUMBER.isValueValid(value)
        ) {
            Icon(
                Icons.Outlined.Add,
                stringResource(R.string.action_increaseValue)
            )
        }
    }
}