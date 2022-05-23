package rahmouni.neil.counters.counter_card.new_increment

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.launch
import rahmouni.neil.counters.IncrementValueType
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.healthConnect


@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun NewIncrement(
    counter: CounterAugmented?,
    countersListViewModel: CountersListViewModel,
    onCreate: () -> (Unit)
) {
    if (counter != null) {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val localHapticFeedback = LocalHapticFeedback.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val scope = rememberCoroutineScope()

        var value by rememberSaveable { mutableStateOf("1") }
        var isValueError by rememberSaveable { mutableStateOf(false) }
        var date: String? by rememberSaveable { mutableStateOf(null) }

        fun validateValue(text: String): Boolean {
            isValueError = text.toIntOrNull() == null
            return !isValueError
        }

        fun addIncrement() {
            if (validateValue(value)) {
                scope.launch {
                    keyboardController?.hide()
                    onCreate()
                    countersListViewModel.addIncrement(
                        value.toInt(),
                        counter.toCounter(),
                        healthConnect.isAvailable() && counter.healthConnectEnabled,
                        date
                    )
                    value = counter.incrementValue.toString()
                }
            }
        }

        LaunchedEffect(counter) {
            value = if (counter.incrementValueType == IncrementValueType.PREVIOUS) {
                counter.lastIncrement.toString()
            } else {
                counter.incrementValue.toString()
            }
        }

        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                FilledTonalIconButton(
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        if (validateValue(value)) {
                            value = (value.toInt() - 1).toString()
                        }
                    },
                    enabled = !isValueError
                ) {
                    Icon(
                        Icons.Outlined.Remove,
                        stringResource(R.string.action_decreaseValue)
                    )
                }
                TextField(
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
                        addIncrement()
                    })
                FilledTonalIconButton(
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        if (validateValue(value)) {
                            value = (value.toInt() + 1).toString()
                        }
                    },
                    enabled = !isValueError
                ) {
                    Icon(
                        Icons.Outlined.Add,
                        stringResource(R.string.action_increaseValue)
                    )
                }
            }

            if (remoteConfig.getBoolean("issue121__new_increment_date")) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = spacedBy(8.dp, Alignment.CenterHorizontally)
                ) {
                    item {
                        EditDateAssistChip(date) { date = it }
                    }
                    item {
                        AssistChip(
                            label = { Text(stringResource(R.string.action_setToLastValue_short)) },
                            enabled = value != counter.lastIncrement.toString(),
                            leadingIcon = { Icon(Icons.Outlined.History, null) },
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                value = counter.lastIncrement.toString()
                            }
                        )
                    }
                }
            }

            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                enabled = !isValueError,
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    addIncrement()
                }) {
                Text(stringResource(R.string.action_addEntry))
            }
        }
    }
}