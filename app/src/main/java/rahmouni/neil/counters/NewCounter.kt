package rahmouni.neil.counters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.analytics.ktx.logEvent
import kotlinx.coroutines.launch
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.database.Counter
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.options.*

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
    var resetType by rememberSaveable { mutableStateOf(ResetType.NEVER) }

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
                .padding(16.dp, 16.dp, 16.dp, bottom = 0.dp),
            onValueChange = {
                name = it
                isNameError = false
            },
            label = { Text(stringResource(R.string.text_name_short) + if (isNameError) "*" else "") },
            singleLine = true,
            isError = isNameError,
            keyboardActions = KeyboardActions {
                if (validateName(name)) keyboardController?.hide()
            },
        )

        CounterStyleOption(counterStyle, true) {
            counterStyle = it
        }
        MenuDefaults.Divider(Modifier.padding(horizontal = 16.dp))
        ButtonBehaviourOption(incrementType, true) {
            if (it != incrementType) {
                incrementValue = it.defaultIncrementValue
            }
            incrementType = it
        }
        if (incrementType == IncrementType.VALUE) {
            MenuDefaults.Divider(Modifier.padding(horizontal = 16.dp))
            MinusEnabledOption(minusEnabled, true) {
                minusEnabled = it
            }
        }
        MenuDefaults.Divider(Modifier.padding(horizontal = 16.dp))
        IncrementValueOption(
            incrementType,
            incrementValueType,
            incrementValue,
            minusEnabled,
            true
        ) { ivt, iv ->
            incrementValueType = ivt
            incrementValue = iv
        }

        MenuDefaults.Divider(Modifier.padding(horizontal = 16.dp))
        ResetTypeOption(resetType, true) {
            resetType = it
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
                            incrementValue = incrementValue,
                            resetType = resetType,
                        )
                        keyboardController?.hide()
                        onCreate()
                        mCountersListViewModel.addCounter(counter)

                        analytics?.logEvent("created_counter") {
                            param("ResetType", resetType.toString())
                        }

                        name = ""
                        minusEnabled = false
                        counterStyle = CounterStyle.DEFAULT
                        incrementType = IncrementType.ASK_EVERY_TIME
                        incrementValue = 1
                        resetType = ResetType.NEVER
                    }
                }
            }) {
            Text(stringResource(R.string.action_create_short))
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun NewCounterExperiment(mCountersListViewModel: CountersListViewModel, onCreate: () -> (Unit)) {
    var name by rememberSaveable { mutableStateOf("") }
    var incrementType by rememberSaveable { mutableStateOf(IncrementType.ASK_EVERY_TIME) }
    var incrementValueType by rememberSaveable { mutableStateOf(IncrementValueType.VALUE) }
    var incrementValue by rememberSaveable { mutableStateOf(1) }
    var isNameError by rememberSaveable { mutableStateOf(false) }
    var minusEnabled by rememberSaveable { mutableStateOf(false) }
    var counterStyle by rememberSaveable { mutableStateOf(CounterStyle.DEFAULT) }
    var resetType by rememberSaveable { mutableStateOf(ResetType.NEVER) }

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
                .padding(16.dp, 16.dp, 16.dp, bottom = 0.dp),
            onValueChange = {
                name = it
                isNameError = false
            },
            label = { Text(stringResource(R.string.text_name_short) + if (isNameError) "*" else "") },
            singleLine = true,
            isError = isNameError,
            keyboardActions = KeyboardActions {
                if (validateName(name)) keyboardController?.hide()
            },
        )

        CounterStyleOption(counterStyle, true) {
            counterStyle = it
        }
        MenuDefaults.Divider(Modifier.padding(horizontal = 16.dp))
        ButtonBehaviourOption(incrementType, true) {
            if (it != incrementType) {
                incrementValue = it.defaultIncrementValue
            }
            incrementType = it
        }
        if (incrementType == IncrementType.VALUE) {
            MenuDefaults.Divider(Modifier.padding(horizontal = 16.dp))
            MinusEnabledOption(minusEnabled, true) {
                minusEnabled = it
            }
        }
        MenuDefaults.Divider(Modifier.padding(horizontal = 16.dp))
        IncrementValueOption(
            incrementType,
            incrementValueType,
            incrementValue,
            minusEnabled,
            true
        ) { ivt, iv ->
            incrementValueType = ivt
            incrementValue = iv
        }

        MenuDefaults.Divider(Modifier.padding(horizontal = 16.dp))
        ResetTypeOption(resetType, true) {
            resetType = it
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
                            incrementValue = incrementValue,
                            resetType = resetType,
                        )
                        keyboardController?.hide()
                        onCreate()
                        mCountersListViewModel.addCounter(counter)

                        analytics?.logEvent("created_counter") {
                            param("ResetType", resetType.toString())
                        }

                        name = ""
                        minusEnabled = false
                        counterStyle = CounterStyle.DEFAULT
                        incrementType = IncrementType.ASK_EVERY_TIME
                        incrementValue = 1
                        resetType = ResetType.NEVER
                    }
                }
            }) {
            Text(stringResource(R.string.action_create_short))
        }
    }
}