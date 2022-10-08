package rahmouni.neil.counters.counterActivity.statCount

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.IncrementGroup

enum class StatCountType(
    val displayName: String,
    val getCount: @Composable (CounterAugmented, CountersListViewModel) -> (Int?)
) {
    THIS_WEEK("This week", { counter, vm ->
        val ig: List<IncrementGroup> by vm.getCounterIncrementGroups(
            counter.uid,
            ResetType.WEEK
        ).observeAsState(listOf())

        if (ig.isEmpty()) null else ig[0].count + counter.resetValue
    }),
    THIS_MONTH("This month", { counter, vm ->
        val ig: List<IncrementGroup> by vm.getCounterIncrementGroups(
            counter.uid,
            ResetType.MONTH
        ).observeAsState(listOf())

        if (ig.isEmpty()) null else ig[0].count + counter.resetValue
    }),
    LAST_WEEK("Last week", { counter, vm ->
        val ig: List<IncrementGroup> by vm.getCounterIncrementGroups(
            counter.uid,
            ResetType.WEEK
        ).observeAsState(listOf())

        if (ig.isEmpty()) null else if (ig.size > 1) ig[1].count + counter.resetValue else 0
    }),
    LAST_MONTH("Last month", { counter, vm ->
        val ig: List<IncrementGroup> by vm.getCounterIncrementGroups(
            counter.uid,
            ResetType.MONTH
        ).observeAsState(listOf())

        if (ig.isEmpty()) null else if (ig.size > 1) ig[1].count + counter.resetValue else 0
    });
}