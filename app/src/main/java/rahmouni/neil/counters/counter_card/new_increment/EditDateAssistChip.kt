package rahmouni.neil.counters.counter_card.new_increment

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.text.format.DateFormat
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.EditCalendar
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import java.text.SimpleDateFormat
import java.util.*
import rahmouni.neil.counters.R

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDateAssistChip(date: String?, setDate: (String?) -> Unit) {
    val localHapticFeedback = LocalHapticFeedback.current

    val c = Calendar.getInstance()

    val context = LocalContext.current
    val mDatePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, y: Int, mo: Int, d: Int ->
            TimePickerDialog(
                context,
                { _: TimePicker, h: Int, mi: Int ->
                    setDate(String.format("%02d-%02d-%02d %02d:%02d:00", y, mo + 1, d, h, mi))
                }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true
            ).show()
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)
    )

    if (date == null) {
        AssistChip(
            label = { Text(stringResource(R.string.editDateAssistChip_assistChip_label)) },
            leadingIcon = { Icon(Icons.Outlined.EditCalendar, null) },
            onClick = {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                mDatePickerDialog.show()
            }
        )
    } else {
        val tmpDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date)
        val title = if (tmpDate != null) DateFormat.format(
            DateFormat.getBestDateTimePattern(
                Locale.getDefault(),
                "d MMMM"
            ), tmpDate
        ).toString()
            .replaceFirstChar { it.uppercase() } + ", " + DateFormat.getTimeFormat(
            context
        ).format(tmpDate) else date

        FilterChip(
            label = { Text(title) },
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