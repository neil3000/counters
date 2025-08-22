package rahmouni.neil.counters.counter_card

import android.content.Intent
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import rahmouni.neil.counters.counterActivity.CounterActivity
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.prefs

@Composable
fun CounterCard(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel?,
    modifier: Modifier = Modifier,
    openNewIncrementSheet: ((countersListViewModel: CountersListViewModel) -> (Unit))?,
) {
    val context = LocalContext.current
    val localHapticFeedback = LocalHapticFeedback.current

    Card(
        colors = CardDefaults.cardColors(containerColor = data.style.getBackGroundColor()),
        modifier = modifier
    ) {
        Column(
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

                    prefs.preferences.edit {
                        putBoolean("LONG_PRESS_TIP_DISMISSED", true)
                    }
                }
            }
        )) {
            if (prefs.debugMode) Text("id:" + data.uid.toString())
            Text(
                text = data.getDisplayName(context),
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
                    it.CardButton(data, countersListViewModel)
                }
                data.valueType.largeDisplay(
                    this,
                    data.getCount(),
                    context,
                    true
                )
                end?.CardButton(data, countersListViewModel)
            }
        }
    }
}