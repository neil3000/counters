package rahmouni.neil.counters.counter_card

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.RemoveCircleOutline
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import rahmouni.neil.counters.IncrementValueType
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.prefs

@OptIn(ExperimentalComposeUiApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun NewIncrement(
    counter: CounterAugmented?,
    countersListViewModel: CountersListViewModel,
    onCreate: () -> (Unit)
) {
    if (counter != null) {
        var value by rememberSaveable { mutableStateOf("1") }

        LaunchedEffect(counter) {
            value = if (counter.incrementValueType == IncrementValueType.PREVIOUS) {
                counter.lastIncrement.toString()
            } else {
                counter.incrementValue.toString()
            }
        }

        var isValueError by rememberSaveable { mutableStateOf(false) }

        val localHapticFeedback = LocalHapticFeedback.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val scope = rememberCoroutineScope()

        fun validateValue(text: String): Boolean {
            isValueError = text.toIntOrNull() == null
            return !isValueError
        }

        Column {
            if (prefs.debugMode) Text("id:" + counter.uid + " | ivt:" + counter.incrementValueType + " | iv:" + counter.incrementValue + " | li:" + counter.lastIncrement)
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                IconButton(onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    if (validateValue(value)) {
                        value = (value.toInt() - 1).toString()
                    }
                }, enabled = !isValueError) {
                    Icon(
                        Icons.Outlined.RemoveCircleOutline,
                        stringResource(R.string.action_decreaseValue)
                    )
                }
                OutlinedTextField(
                    value = value,
                    onValueChange = { str ->
                        isValueError = false
                        value = str
                    },
                    singleLine = true,
                    modifier = Modifier.widthIn(0.dp, 150.dp),
                    isError = isValueError,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    keyboardActions = KeyboardActions {
                        if (validateValue(value)) keyboardController?.hide()
                    })
                IconButton(onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    if (validateValue(value)) {
                        value = (value.toInt() + 1).toString()
                    }
                }, enabled = !isValueError) {
                    Icon(
                        Icons.Outlined.AddCircleOutline,
                        stringResource(R.string.action_increaseValue)
                    )
                }
            }
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                enabled = !isValueError,
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    if (validateValue(value)) {
                        scope.launch {
                            keyboardController?.hide()
                            onCreate()
                            countersListViewModel.addIncrement(value.toInt(), counter.uid)
                            value = counter.incrementValue.toString()
                        }
                    }
                }) {
                Text(stringResource(R.string.action_addEntry_short))
            }
        }
    }
}