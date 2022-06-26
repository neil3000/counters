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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType.Companion.LongPress
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.healthConnect

@Composable
fun CounterCardButtons(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel?
) {
    if (data.minusButtonEnabled) return CounterCardButtonsMinus(data, countersListViewModel)
    return CounterCardButtonsDefault(data, countersListViewModel)
}

@Composable
fun CounterCardButtonsDefault(
    data: CounterAugmented,
    countersListViewModel: CountersListViewModel?,
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
    ) {
        data.valueType.largeDisplay(this, data.count + data.resetValue, context)
        IconButton(
            modifier = Modifier.testTag(data.displayName + "_CARD_INCREASE"),
            onClick = {
                if (countersListViewModel != null) {
                    localHapticFeedback.performHapticFeedback(LongPress)

                    countersListViewModel.addIncrement(
                        data.plusButtonValue,
                        data.toCounter(),
                        healthConnect.isAvailable() && data.healthConnectEnabled
                    )
                }
            }) {
            Icon(
                imageVector = Icons.Outlined.Add,
                contentDescription = stringResource(R.string.action_increase)
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

    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = {
            if (countersListViewModel != null) {
                localHapticFeedback.performHapticFeedback(LongPress)

                countersListViewModel.addIncrement(
                    -data.plusButtonValue,
                    data.toCounter(),
                    healthConnect.isAvailable() && data.healthConnectEnabled
                )
            }
        }) {
            Icon(
                imageVector = Icons.Outlined.Remove,
                contentDescription = stringResource(R.string.action_decrease),
            )
        }
        data.valueType.largeDisplay(this, data.count + data.resetValue, context)
        IconButton(onClick = {
            if (countersListViewModel != null) {
                localHapticFeedback.performHapticFeedback(LongPress)

                countersListViewModel.addIncrement(
                    data.plusButtonValue,
                    data.toCounter(),
                    healthConnect.isAvailable() && data.healthConnectEnabled
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