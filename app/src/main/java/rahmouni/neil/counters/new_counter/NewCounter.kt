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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.IncrementType
import rahmouni.neil.counters.IncrementValueType
import rahmouni.neil.counters.database.Counter
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.new_counter.color_selector.CounterStyleSelector
import rahmouni.neil.counters.new_counter.options.ButtonBehaviourOption
import rahmouni.neil.counters.new_counter.options.IncrementValueOption
import rahmouni.neil.counters.new_counter.options.MinusEnabledOption

@OptIn(ExperimentalComposeUiApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun NewCounter(mCountersListViewModel: CountersListViewModel, onCreate: () -> (Unit)) {
    var name by rememberSaveable { mutableStateOf("") }
    var incrementType by rememberSaveable { mutableStateOf(IncrementType.ASK_EVERY_TIME) }
    var incrementValueType by rememberSaveable { mutableStateOf(IncrementValueType.VALUE) }
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
        ButtonBehaviourOption(incrementType) {
            if (it!=incrementType) {
                incrementValue = it.defaultIncrementValue
            }
            incrementType = it
        }
        if (incrementType == IncrementType.VALUE) {
            Divider(Modifier.padding(horizontal = 16.dp))
            MinusEnabledOption(minusEnabled) {
                minusEnabled = it
            }
        }
        Divider(Modifier.padding(horizontal = 16.dp))
        IncrementValueOption(incrementType, incrementValueType, incrementValue, minusEnabled) { ivt, iv ->
            incrementValueType = ivt
            incrementValue = iv
        }

        Button(
            modifier = Modifier
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
                            incrementValueType = incrementValueType,
                            incrementValue = incrementValue
                        )
                        keyboardController?.hide()
                        onCreate()
                        mCountersListViewModel.addCounter(counter)

                        name = ""
                        minusEnabled = false
                        counterStyle = CounterStyle.DEFAULT
                        incrementType = IncrementType.ASK_EVERY_TIME
                        incrementValue = 1
                    }
                }
            }) {
            Text("Create") //TODO i18n
        }
    }
}