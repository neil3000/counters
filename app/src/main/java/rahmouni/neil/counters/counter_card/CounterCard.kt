package rahmouni.neil.counters.counter_card

import android.content.Intent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterCard(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel,
    openNewIncrementSheet: (countersListViewModel: CountersListViewModel) -> (Unit)
) {
    val context = LocalContext.current
    val localHapticFeedback = LocalHapticFeedback.current

    Card(
        containerColor = data.style.getBackGroundColor(),
        onClick = {
            localHapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)

            context.startActivity(
                Intent(context, CounterActivity::class.java).putExtra(
                    "counterID",
                    data.uid
                )
            )
        }
    ) {
        Column {
            if (prefs.debugMode) Text("id:" + data.uid.toString())
            Text(
                text = data.displayName,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(all = 8.dp)
            )
            CounterCardButtons(data, countersListViewModel) {
                openNewIncrementSheet(
                    countersListViewModel
                )
            }
        }
    }
}