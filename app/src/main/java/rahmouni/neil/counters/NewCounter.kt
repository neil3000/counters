package rahmouni.neil.counters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material3.*
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
import rahmouni.neil.counters.options.CounterStyleOption
import rahmouni.neil.counters.options.ValueOption
import rahmouni.neil.counters.utils.tiles.TileDialogRadioButtons
import rahmouni.neil.counters.utils.tiles.tile_color_selection.Size
import rahmouni.neil.counters.value_types.ValueType

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NewCounter(mCountersListViewModel: CountersListViewModel, onCreate: () -> (Unit)) {
    var name by rememberSaveable { mutableStateOf("") }
    var counterStyle by rememberSaveable { mutableStateOf(CounterStyle.DEFAULT) }
    var valueType by rememberSaveable { mutableStateOf(ValueType.NUMBER) }
    var resetType by rememberSaveable { mutableStateOf(ResetType.NEVER) }
    var resetValue by rememberSaveable { mutableStateOf(0) }

    val localHapticFeedback = LocalHapticFeedback.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()
    //val remoteConfig = FirebaseRemoteConfig.getInstance()

    Column(Modifier.fillMaxWidth()) {
        TextField(
            value = name,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, bottom = 8.dp),
            onValueChange = {
                name = it
            },
            label = { Text(stringResource(R.string.newCounter_textField_name_label)) },
            singleLine = true,
            keyboardActions = KeyboardActions {
                keyboardController?.hide()
            },
        )

        CounterStyleOption(counterStyle, Size.SMALL) {
            counterStyle = it
        }

        TileDialogRadioButtons(
            title = stringResource(R.string.newCounter_tile_valueType_title),
            icon = Icons.Outlined.Category,
            values = ValueType.values().toList(),
            selected = valueType
        ) {
            valueType = it as ValueType
        }
        Divider(Modifier.padding(horizontal = 16.dp))
        TileDialogRadioButtons(
            title = stringResource(R.string.newCounter_tile_resetFrequency_title),
            dialogTitle = stringResource(R.string.newCounter_tile_resetFrequency_dialogTitle),
            icon = Icons.Outlined.Event,
            values = ResetType.values().toList(),
            selected = resetType
        ) {
            resetType = it as ResetType
        }
        Divider(Modifier.padding(horizontal = 16.dp))
        ValueOption(
            title = stringResource(R.string.counterSettings_tile_resetValue_title),
            secondaryFormatter = R.string.counterSettings_tile_resetValue_secondaryFormatter,
            icon = Icons.Outlined.Pin,
            dialogTitle = stringResource(R.string.counterSettings_tile_resetValue_dialogTitle),
            valueType,
            resetValue,
            resetType != ResetType.NEVER
        ) {
            resetValue = it
        }

        Button(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            onClick = {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                scope.launch {
                    val counter = Counter(
                        displayName = if (name.isBlank()) null else name,
                        style = counterStyle,
                        valueType = valueType,
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
                    valueType = ValueType.NUMBER
                    resetType = ResetType.NEVER
                    resetValue = 0
                }
            }) {
            Text(stringResource(R.string.newCounter_button_create_text))
        }
    }
}