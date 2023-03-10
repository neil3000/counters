package rahmouni.neil.counters.counter_card.new_increment

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import rahmouni.neil.counters.R
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDateAssistChip(date: Long?, setDate: (Long?) -> Unit) {
    val localHapticFeedback = LocalHapticFeedback.current

    val openDateDialog = remember { mutableStateOf(false) }
    val openTimeDialog = remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    if (date == null) {
        AssistChip(
            label = { Text(stringResource(R.string.editDateAssistChip_assistChip_label)) },
            leadingIcon = { Icon(Icons.Outlined.EditCalendar, null) },
            onClick = {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                openDateDialog.value = true
            }
        )
        if (openDateDialog.value) {
            DatePickerDialog(
                onDismissRequest = {
                    openDateDialog.value = false
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openDateDialog.value = false
                            openTimeDialog.value = true

                            /*TimePickerDialog(
                                context,
                                { _: TimePicker, h: Int, mi: Int ->
                                    setDate(
                                        datePickerState.selectedDateMillis?.plus(mi * 1000 * 60)
                                            ?.plus((h-1) * 1000 * 60 * 60)
                                    )
                                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true
                            ).show()*/
                        },
                        enabled = datePickerState.selectedDateMillis != null
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDateDialog.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        } else if (openTimeDialog.value) {
            AlertDialog(
                onDismissRequest = {
                    openTimeDialog.value = false
                },
                text = {
                    TimePicker(state = timePickerState)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            openTimeDialog.value = false

                            setDate(
                                datePickerState.selectedDateMillis?.plus(timePickerState.minute * 1000 * 60)
                                    ?.plus(timePickerState.hour * 1000 * 60 * 60)?.minus(
                                        TimeZone.getDefault().getOffset(System.currentTimeMillis())
                                    )
                            )

                        }
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            openDateDialog.value = false
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    } else {
        val b = SimpleDateFormat("MMMM dd, HH:mm").format(date)

        FilterChip(
            label = { Text(b) },
            selected = true,
            trailingIcon = {
                Icon(
                    Icons.Outlined.Close,
                    stringResource(R.string.editDateAssistChip_filterChip_trailingIcon_contentDescription),
                    Modifier.scale(.8f)
                )
            },
            onClick = {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                setDate(null)
            }
        )
    }
}