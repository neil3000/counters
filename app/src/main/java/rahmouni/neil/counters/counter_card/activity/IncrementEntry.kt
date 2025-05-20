package rahmouni.neil.counters.counter_card.activity

import android.annotation.SuppressLint
import android.text.format.DateFormat
import android.text.format.DateUtils
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.Increment
import rahmouni.neil.counters.utils.dialogs.ConfirmationDialog
import rahmouni.neil.counters.value_types.ValueType

@SuppressLint("SimpleDateFormat")
@Composable
fun IncrementEntry(
    increment: Increment,
    countersListViewModel: CountersListViewModel,
    valueType: ValueType,
    resetType: ResetType = ResetType.NEVER
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(increment.timestamp)

    ListItem(
        leadingContent = {
            Surface(
                Modifier.size(40.dp),
                color = MaterialTheme.colorScheme.secondaryContainer,
                shape = RoundedCornerShape(8.dp)
            ) {
                valueType.smallDisplay(increment.value, context)
            }
        },
        headlineContent = {
            if (date != null) {
                val formattedDate = when (resetType) {
                    ResetType.NEVER -> DateUtils.getRelativeTimeSpanString(date.time).toString()
                    ResetType.DAY -> DateFormat.getTimeFormat(context).format(date)
                    ResetType.WEEK -> DateFormat.format(
                        DateFormat.getBestDateTimePattern(
                            Locale.getDefault(),
                            "EEEE"
                        ), date
                    ).toString()
                        .replaceFirstChar { it.uppercase() } + ", " + DateFormat.getTimeFormat(
                        context
                    ).format(date)
                    ResetType.MONTH -> DateFormat.format(
                        DateFormat.getBestDateTimePattern(
                            Locale.getDefault(),
                            "MMMM d"
                        ), date
                    ).toString()
                        .replaceFirstChar { it.uppercase() } + ", " + DateFormat.getTimeFormat(
                        context
                    ).format(date)
                }

                Text(formattedDate)
            }
        },
        supportingContent = if (increment.notes != null) {
            { Text(increment.notes) }
        } else null,
        trailingContent = {
            ConfirmationDialog(
                title = stringResource(R.string.incrementEntry_confirmationDialog_title),
                body = { Text(stringResource(R.string.incrementEntry_confirmationDialog_body_text)) },
                icon = Icons.Outlined.DeleteForever,
                confirmLabel = stringResource(R.string.incrementEntry_confirmationDialog_confirmLabel),
                onConfirm = {
                    scope.launch {
                        countersListViewModel.deleteIncrement(increment)
                    }
                }
            ) {
                IconButton(onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    it()
                }) {
                    Icon(
                        Icons.Outlined.Delete,
                        stringResource(R.string.incrementEntry_icon_delete_contentDescription),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    )
}