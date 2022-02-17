package rahmouni.neil.counters.counter_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.LongPress
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import rahmouni.neil.counters.IncrementType
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel

@Composable
fun CounterCardButtons(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel,
    openNewIncrementSheet: () -> (Unit)
) {
    if (data.hasMinus) return CounterCardButtonsMinus(data, countersListViewModel)
    return CounterCardButtonsDefault(data, countersListViewModel) { openNewIncrementSheet() }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CounterCardButtonsDefault(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel,
    openNewIncrementSheet: () -> (Unit)
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
    ) {
        Text(
            text = data.count.toString(),
            style = MaterialTheme.typography.headlineLarge,
        )
        IconButton(onClick = {
            localHapticFeedback.performHapticFeedback(LongPress)

            when (data.incrementType) {
                IncrementType.ASK_EVERY_TIME -> scope.launch { openNewIncrementSheet() }
                IncrementType.VALUE -> countersListViewModel.addIncrement(
                    data.incrementValue,
                    data.uid
                )
            }
        }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Increase", //TODO i18n
            )
        }
    }
}


@Composable
fun CounterCardButtonsMinus(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel
) {
    val localHapticFeedback = LocalHapticFeedback.current

    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = {
            localHapticFeedback.performHapticFeedback(LongPress)
            countersListViewModel.addIncrement(-data.incrementValue, data.uid)
        }) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = "Decrease", //TODO i18n
            )
        }
        Text(
            text = data.count.toString(),
            style = MaterialTheme.typography.headlineLarge,
        )
        IconButton(onClick = {
            localHapticFeedback.performHapticFeedback(LongPress)
            countersListViewModel.addIncrement(data.incrementValue, data.uid)
        }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Increase", //TODO i18n
            )
        }
    }
}