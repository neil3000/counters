package rahmouni.neil.counters.counter_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.LongPress
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import rahmouni.neil.counters.IncrementType
import rahmouni.neil.counters.IncrementValueType
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.healthConnect
import kotlin.math.abs

@Composable
fun CounterCardButtons(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel?,
    openNewIncrementSheet: () -> (Unit)
) {
    if (data.hasMinus) return CounterCardButtonsMinus(data, countersListViewModel)
    return CounterCardButtonsDefault(data, countersListViewModel) { openNewIncrementSheet() }
}

@Composable
fun CounterCardButtonsDefault(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel?,
    openNewIncrementSheet: () -> (Unit)
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val incValue =
        if (data.incrementValueType == IncrementValueType.VALUE) data.incrementValue else data.lastIncrement

    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
    ) {
        Text(
            text = (data.count + data.resetValue).toString(),
            style = MaterialTheme.typography.headlineLarge,
        )
        IconButton(
            modifier = Modifier.testTag(data.displayName + if (incValue >= 0) "_CARD_INCREASE" else "_CARD_DECREASE"),
            onClick = {
                if (countersListViewModel != null) {
                    localHapticFeedback.performHapticFeedback(LongPress)

                    when (data.incrementType) {
                        IncrementType.ASK_EVERY_TIME -> scope.launch { openNewIncrementSheet() }
                        IncrementType.VALUE -> countersListViewModel.addIncrement(
                            incValue,
                            data.uid,
                            healthConnect.isAvailable(context) && data.healthConnectEnabled
                        )
                    }
                }
            }) {
            Icon(
                imageVector = if (incValue >= 0) Icons.Outlined.Add else Icons.Outlined.Remove,
                contentDescription = stringResource(if (incValue >= 0) R.string.action_increase else R.string.action_decrease),
            )
        }
    }
}


@Composable
fun CounterCardButtonsMinus(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel?
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    val incValue =
        abs(if (data.incrementValueType == IncrementValueType.VALUE) data.incrementValue else data.lastIncrement)

    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = {
            if (countersListViewModel != null) {
                localHapticFeedback.performHapticFeedback(LongPress)

                countersListViewModel.addIncrement(
                    -incValue,
                    data.uid,
                    healthConnect.isAvailable(context) && data.healthConnectEnabled
                )
            }
        }) {
            Icon(
                imageVector = Icons.Outlined.Remove,
                contentDescription = stringResource(R.string.action_decrease),
            )
        }
        Text(
            text = (data.count + data.resetValue).toString(),
            style = MaterialTheme.typography.headlineLarge,
        )
        IconButton(onClick = {
            if (countersListViewModel != null) {
                localHapticFeedback.performHapticFeedback(LongPress)

                countersListViewModel.addIncrement(
                    incValue,
                    data.uid,
                    healthConnect.isAvailable(context) && data.healthConnectEnabled
                )
            }
        }) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = stringResource(R.string.action_increase),
            )
        }
    }
}