package rahmouni.neil.counters.counterActivity.statCount

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import rahmouni.neil.counters.R
import rahmouni.neil.counters.goals.ResetType
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.IncrementGroup

enum class StatCountType(
    val displayName: Int,
    val getCount: @Composable (CounterAugmented, CountersListViewModel) -> (Int?)
) {
    THIS_WEEK(R.string.statCountType_thisWeek_title, { counter, _ ->
        counter.getRawCount(ResetType.WEEK)
    }),
    THIS_MONTH(R.string.statCountType_thisMonth_title, { counter, _ ->
        counter.getRawCount(ResetType.MONTH)
    }),
    LAST_WEEK(R.string.statCountType_lastWeek_title, { counter, vm ->
        val ig: List<IncrementGroup> by vm.getCounterIncrementGroups(
            counter.uid,
            ResetType.WEEK
        ).observeAsState(listOf())

        if (ig.isEmpty()) null else if (ig.size > 1) ig[1].count else 0
    }),
    LAST_MONTH(R.string.statCountType_lastMonth_title, { counter, vm ->
        val ig: List<IncrementGroup> by vm.getCounterIncrementGroups(
            counter.uid,
            ResetType.MONTH
        ).observeAsState(listOf())

        if (ig.isEmpty()) null else if (ig.size > 1) ig[1].count else 0
    });
}