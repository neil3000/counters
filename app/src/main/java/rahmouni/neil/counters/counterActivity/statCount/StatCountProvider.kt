package rahmouni.neil.counters.counterActivity.statCount

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import rahmouni.neil.counters.goals.ResetType
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel

@Composable
fun StatCountProvider(
    counter: CounterAugmented,
    countersListViewModel: CountersListViewModel
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        var start = StatCountType.THIS_WEEK
        var end = StatCountType.THIS_MONTH

        when (counter.resetType) {
            ResetType.WEEK -> {
                start = StatCountType.LAST_WEEK
                end = StatCountType.THIS_MONTH
            }
            ResetType.MONTH -> {
                start = StatCountType.LAST_MONTH
                end = StatCountType.THIS_WEEK
            }
            else -> {}
        }

        StatCount(
            counter = counter,
            countersListViewModel = countersListViewModel,
            type = start
        )
        StatCount(
            counter = counter,
            countersListViewModel = countersListViewModel,
            type = end
        )
    }
}