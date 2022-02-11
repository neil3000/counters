package rahmouni.neil.counters.new_counter

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import rahmouni.neil.counters.Counter
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.IncrementType
import rahmouni.neil.counters.counterDao
import rahmouni.neil.counters.new_counter.color_selector.CounterStyleSelector
import rahmouni.neil.counters.new_counter.options.DefaultIncrementOption
import rahmouni.neil.counters.new_counter.options.MinusEnabledOption
import rahmouni.neil.counters.ui.theme.CountersTheme

@OptIn(ExperimentalComposeUiApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun NewCounter(onCreate: (Counter) -> (Unit)) {
    var name by rememberSaveable { mutableStateOf("") }
    var incrementType by rememberSaveable { mutableStateOf(IncrementType.VALUE) }
    var incrementValue by rememberSaveable { mutableStateOf(1) }
    var isNameError by rememberSaveable { mutableStateOf(false) }
    var minusEnabled by rememberSaveable { mutableStateOf(false) }
    var counterStyle by rememberSaveable { mutableStateOf(CounterStyle.DEFAULT) }

    val localHapticFeedback = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    fun validateName(text: String): Boolean {
        isNameError = text.count() < 1
        return !isNameError
    }

    Column(Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            onValueChange = {
                name = it
                isNameError = false
            },
            label = { Text(if (isNameError) "Name*" else "Name") }, //TODO i18n
            singleLine = true,
            isError = isNameError,
            keyboardActions = KeyboardActions {
                if (validateName(name)) keyboardController?.hide()
            },
        )

        CounterStyleSelector(counterStyle) {
            counterStyle = it
        }

        Divider(Modifier.padding(16.dp, 16.dp, 16.dp, 0.dp))
        DefaultIncrementOption(incrementType, incrementValue) { it, iv ->
            incrementType = it
            incrementValue = iv
        }
        Divider(Modifier.padding(horizontal = 16.dp))
        MinusEnabledOption(minusEnabled) {
            minusEnabled = it
        }

        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(16.dp)
                .fillMaxWidth(),
            enabled = !isNameError,
            onClick = {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                if (validateName(name)) {
                    scope.launch {
                        val counter = Counter(
                            displayName = name,
                            hasMinus = minusEnabled,
                            style = counterStyle,
                            incrementType = incrementType,
                            incrementValue = incrementValue
                        )
                        keyboardController?.hide()
                        onCreate(counter.copy(uid = (counterDao?.addCounter(counter) ?: 0).toInt()))

                        name = ""
                        minusEnabled = false
                        counterStyle = CounterStyle.DEFAULT
                        incrementType = IncrementType.VALUE
                        incrementValue = 1
                    }
                }
            }) {
            Text("Create") //TODO i18n
        }
    }
}

@Preview(showBackground = true)
@Composable
fun NewCounterPreview() {
    CountersTheme {
        NewCounter {}
    }
}