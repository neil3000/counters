package rahmouni.neil.counters.counter_card.new_increment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.launch
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.health_connect.HealthConnectAvailability
import rahmouni.neil.counters.health_connect.HealthConnectManager

@OptIn(
    ExperimentalComposeUiApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun NewIncrement(
    counter: CounterAugmented?,
    countersListViewModel: CountersListViewModel,
    healthConnectManager: HealthConnectManager,
    onCreate: () -> (Unit)
) {
    if (counter != null) {
        //val remoteConfig = FirebaseRemoteConfig.getInstance()
        val localHapticFeedback = LocalHapticFeedback.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val scope = rememberCoroutineScope()
        val context = LocalContext.current

        var value by rememberSaveable { mutableStateOf("1") }
        var date: String? by rememberSaveable { mutableStateOf(null) }
        var areNotesVisible: Boolean by rememberSaveable { mutableStateOf(false) }
        var notes: String? by rememberSaveable { mutableStateOf(null) }

        fun reset() {
            value = counter.lastIncrement.toString()
            date = null
            areNotesVisible = false
            notes = null
        }

        fun addIncrement() {
            if (counter.valueType.isValueValid(value)) {
                scope.launch {
                    keyboardController?.hide()
                    onCreate()
                    countersListViewModel.addIncrement(
                        value.toInt(),
                        counter,
                        context,
                        healthConnectManager,
                        date,
                        if (areNotesVisible) notes else null
                    )
                    reset()
                }
            }
        }

        LaunchedEffect(counter) {
            reset()
        }

        Column {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                counter.valueType.picker(value) {
                    value = it
                }
            }

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                item { Box(Modifier.width(16.dp)) }
                item {
                    EditDateAssistChip(date) { date = it }
                }
                item {
                    if (!areNotesVisible) {
                        AssistChip(
                            label = { Text(stringResource(R.string.newIncrement_chip_addNotes_label)) },
                            leadingIcon = { Icon(Icons.Outlined.EditNote, null) },
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                areNotesVisible = true
                            }
                        )
                    } else {
                        // ClearNotes
                        FilterChip(
                            label = { Text(stringResource(R.string.newIncrement_chip_addNotes_label)) },
                            selected = true,
                            trailingIcon = {
                                Icon(
                                    Icons.Outlined.Close,
                                    stringResource(R.string.newIncrement_filterChip_clearNotes_trailingIcon_contentDescription),
                                    Modifier.scale(.8f)
                                )
                            },
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                areNotesVisible = false
                            }
                        )
                    }
                }
                item {
                    AssistChip(
                        label = { Text(stringResource(R.string.newIncrement_chip_lastValue_label)) },
                        enabled = value != counter.lastIncrement.toString(),
                        leadingIcon = { Icon(Icons.Outlined.History, null) },
                        onClick = {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            value = counter.lastIncrement.toString()
                        }
                    )
                }
                item { Box(Modifier.width(16.dp)) }
            }

            AnimatedVisibility(visible = areNotesVisible) {
                Divider(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp))

                TextField(
                    value = notes ?: "",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    label = { Text(stringResource(R.string.newIncrement_textField_notes_label)) },
                    onValueChange = { str ->
                        notes = if (str == "") null else str
                    },
                    singleLine = true,
                    keyboardActions = KeyboardActions {
                        keyboardController?.hide()
                    }
                )
            }

            // AddIncrement
            Button(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                enabled = counter.valueType.isValueValid(value),
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    addIncrement()
                }) {
                Text(stringResource(R.string.newIncrement_button_addIncrement_text))
            }
        }
    }
}