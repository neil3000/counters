package rahmouni.neil.counters.value_types

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.Counter
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.utils.tiles.TileClick
import rahmouni.neil.counters.utils.tiles.TileEndSwitch
import rahmouni.neil.counters.utils.tiles.TileSwitch

class ValueTypeButton(
    private val title: String,
    private val icon: ImageVector,
    val valueType: ValueType,
    val isEnabled: (Counter) -> Boolean,
    private val value: (Counter) -> Int,
    private val updatedCounter: (counter: Counter, enabled: Boolean) -> Counter,
    private val updatedCounterValue: ((counter: Counter, value: Int) -> Counter)? = null,
) {
    @Composable
    fun Tile(counter: Counter, countersListViewModel: CountersListViewModel) {
        val keyboardController = LocalSoftwareKeyboardController.current
        val localHapticFeedback = LocalHapticFeedback.current

        var checked by rememberSaveable { mutableStateOf(isEnabled(counter)) }

        fun onToggle(value: Boolean) {
            checked = value

            countersListViewModel.updateCounter(updatedCounter(counter, value))
        }

        if (updatedCounterValue != null) {
            var openDialog by rememberSaveable { mutableStateOf(false) }
            var dialogValue by rememberSaveable { mutableStateOf(value(counter).toString()) }

            fun closeDialog() {
                keyboardController?.hide()
                openDialog = false
                dialogValue = value(counter).toString()
            }

            fun confirm() {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                if (valueType.isValueValid(dialogValue)) {
                    countersListViewModel.updateCounter(
                        (updatedCounterValue)(
                            counter,
                            dialogValue.toInt()
                        )
                    )

                    keyboardController?.hide()
                    openDialog = false
                }
            }

            if (openDialog) {
                AlertDialog(
                    onDismissRequest = {
                        closeDialog()
                    },
                    title = {
                        Text(stringResource(R.string.cardSettingsActivity_dialog_editButtonValue_title))
                    },
                    icon = { Icon(Icons.Outlined.Edit, null) },
                    text = {
                        valueType.picker(dialogValue) {
                            dialogValue = it
                        }
                    },
                    confirmButton = {
                        TextButton(
                            enabled = valueType.isValueValid(dialogValue),
                            onClick = {
                                confirm()
                            }
                        ) {
                            Text(stringResource(R.string.valueTypeButton_dialog_confirmButton_text))
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                closeDialog()
                            }
                        ) {
                            Text(stringResource(R.string.valueTypeButton_dialog_dismissButton_text))
                        }
                    }
                )
            }

            TileEndSwitch(checked = checked, onChange = { onToggle(it) }) {
                TileClick(
                    title = stringResource(
                        R.string.cardSettingsActivity_tile_showXbutton_title,
                        title
                    ),
                    icon = icon,
                    modifier = it,
                    description = stringResource(
                        R.string.cardSettingsActivity_tile_editButtonValue_description,
                        value(counter)
                    )
                ) {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    openDialog = true
                }
            }
        } else {
            TileSwitch(
                title = stringResource(R.string.cardSettingsActivity_tile_showXbutton_title, title),
                icon = icon,
                checked = checked
            ) {
                onToggle(it)
            }
        }
    }

    @Composable
    fun CardButton(
        counter: CounterAugmented,
        countersListViewModel: CountersListViewModel?
    ) {
        val haptic = LocalHapticFeedback.current

        IconButton(onClick = {
            if (countersListViewModel != null) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                countersListViewModel.addIncrement(
                    value(counter.toCounter()),
                    counter
                )
            }
        }) {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        }
    }
}