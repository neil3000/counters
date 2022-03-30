package rahmouni.neil.counters.counter_card.activity

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterWithIncrements
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.IncrementGroup
import rahmouni.neil.counters.utils.FullscreenDynamicSVG

@Composable
fun CounterEntries(
    counterWithIncrements: CounterWithIncrements?,
    countersListViewModel: CountersListViewModel,
    innerPadding: PaddingValues
) {
    if (counterWithIncrements != null && counterWithIncrements.increments.isNotEmpty()) {
        if (counterWithIncrements.counter.resetType.entriesGroup1 == null) {
            LazyColumn(contentPadding = innerPadding, reverseLayout = true) {
                items(counterWithIncrements.increments) { increment ->
                    MenuDefaults.Divider()
                    IncrementEntry(increment, countersListViewModel)
                }
            }
        } else {
            val incrementGroups: List<IncrementGroup>? by countersListViewModel.getCounterIncrementGroups(
                counterWithIncrements.counter.uid,
                counterWithIncrements.counter.resetType
            ).observeAsState()

            if (incrementGroups != null) {
                LazyColumn(contentPadding = innerPadding, reverseLayout = true) {
                    itemsIndexed(incrementGroups!!) { it, incrementGroup ->
                        Column {
                            for (increment in counterWithIncrements.increments.filter {
                                incrementGroup.uids.split(
                                    ','
                                ).contains(it.uid.toString())
                            }) {
                                IncrementEntry(increment, countersListViewModel)
                                MenuDefaults.Divider()
                            }
                        }
                        Text(
                            stringResource(R.string.text_thisMonthX, incrementGroup.count),
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(top = 24.dp, start=8.dp, end=8.dp)
                        )
                    }
                }
            }
        }
    } else {
        FullscreenDynamicSVG(
            R.drawable.ic_empty_entries,
            R.string.text_noEntriesYet
        )
    }
}