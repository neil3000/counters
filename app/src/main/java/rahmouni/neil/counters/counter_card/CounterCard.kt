package rahmouni.neil.counters.counter_card

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.counter_card.activity.CounterActivity
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.prefs

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CounterCard(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel?,
    openNewIncrementSheet: ((countersListViewModel: CountersListViewModel) -> (Unit))?
) {
    val context = LocalContext.current
    val localHapticFeedback = LocalHapticFeedback.current

    Card(
        colors = CardDefaults.cardColors(containerColor = data.style.getBackGroundColor()),
        modifier = Modifier.combinedClickable(
            onClick = {
                if (openNewIncrementSheet != null) {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    context.startActivity(
                        Intent(context, CounterActivity::class.java).putExtra(
                            "counterID",
                            data.uid
                        )
                    )
                }
            },
            onLongClick = {
                if (openNewIncrementSheet != null && countersListViewModel != null) {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    openNewIncrementSheet(
                        countersListViewModel
                    )
                }
            }
        )
    ) {
        Column {
            if (prefs.debugMode) Text("id:" + data.uid.toString())
            Text(
                text = data.displayName,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(8.dp)
            )
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                val buttons =
                    data.valueType.getButtons().filter { it.isEnabled(data.toCounter()) }
                        .toMutableList()
                val end = buttons.removeFirstOrNull()

                buttons.forEach {
                    it.CardButton(data.toCounter(), countersListViewModel)
                }
                data.valueType.largeDisplay(this, data.count + data.resetValue, context)
                end?.CardButton(data.toCounter(), countersListViewModel)
            }
        }
    }
}