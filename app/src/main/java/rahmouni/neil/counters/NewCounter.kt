package rahmouni.neil.counters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material3.Button
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.launch
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.database.Counter
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.options.CounterStyleOption
import rahmouni.neil.counters.utils.tiles.TileDialogRadioButtons
import rahmouni.neil.counters.utils.tiles.TileNumberInput
import rahmouni.neil.counters.utils.tiles.tile_color_selection.Size

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun NewCounter(mCountersListViewModel: CountersListViewModel, onCreate: () -> (Unit)) {
    var name by rememberSaveable { mutableStateOf("") }
    var isNameError by rememberSaveable { mutableStateOf(false) }
    var counterStyle by rememberSaveable { mutableStateOf(CounterStyle.DEFAULT) }
    var resetType by rememberSaveable { mutableStateOf(ResetType.NEVER) }
    var resetValue by rememberSaveable { mutableStateOf(0) }

    val localHapticFeedback = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    fun validateName(text: String): Boolean {
        isNameError = text.isEmpty()
        return !isNameError
    }

    Column(Modifier.fillMaxWidth()) {
        TextField(
            value = name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, bottom = 8.dp),
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

        CounterStyleOption(counterStyle, Size.SMALL) {
            counterStyle = it
        }

        MenuDefaults.Divider(Modifier.padding(horizontal = 16.dp))
        TileDialogRadioButtons(
            title = stringResource(R.string.text_resetFrequency),
            dialogTitle = stringResource(R.string.text_reset),
            icon = Icons.Outlined.Event,
            values = ResetType.values().toList(),
            selected = resetType
        ) {
            resetType = it as ResetType
        }

        if (remoteConfig.getBoolean("issue54__reset_value_setting")) {
            MenuDefaults.Divider(Modifier.padding(horizontal = 16.dp))
            TileNumberInput(
                title = stringResource(R.string.text_resetValue),
                dialogTitle = stringResource(R.string.action_resetTo),
                icon = Icons.Outlined.Pin,
                value = resetValue,
                format = R.string.text_resetsToX,
                enabled = resetType != ResetType.NEVER
            ) {
                resetValue = it
            }
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
                            style = counterStyle,
                            resetType = resetType,
                            resetValue = resetValue
                        )
                        keyboardController?.hide()
                        onCreate()
                        mCountersListViewModel.addCounter(counter)

                        analytics?.logEvent("created_counter") {
                            param("Style", counterStyle.toString())
                            param("ResetType", resetType.toString())
                            param("ResetValue", resetValue.toLong())
                        }

                        name = ""
                        counterStyle = CounterStyle.DEFAULT
                        resetType = ResetType.NEVER
                        resetValue = 0
                    }
                }
            }) {
            Text(stringResource(R.string.action_create_short))
        }
    }
}