package rahmouni.neil.counters.counter_card.activity

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import rahmouni.neil.counters.R
import rahmouni.neil.counters.counter_card.IncrementEntry
import rahmouni.neil.counters.database.CounterWithIncrements
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.utils.FullscreenDynamicSVG

@Composable
fun CounterEntries(
    counterWithIncrements: CounterWithIncrements?,
    countersListViewModel: CountersListViewModel,
    innerPadding: PaddingValues
) {
    if (counterWithIncrements != null && counterWithIncrements.increments.isNotEmpty()) {
        LazyColumn(contentPadding = innerPadding) {
            items(counterWithIncrements.increments.reversed()) { increment ->
                IncrementEntry(increment, countersListViewModel)
                MenuDefaults.Divider()
            }
        }
    } else {
        FullscreenDynamicSVG(
            R.drawable.ic_empty_entries,
            R.string.text_noEntriesYet
        )
    }
}