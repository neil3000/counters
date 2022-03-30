package rahmouni.neil.counters.counter_card.activity

import android.annotation.SuppressLint
import android.text.format.DateFormat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.database.CounterWithIncrements
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.IncrementGroup
import rahmouni.neil.counters.utils.FullscreenDynamicSVG
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
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
                LazyColumn(contentPadding = innerPadding) {
                    itemsIndexed(incrementGroups!!) { it, incrementGroup ->
                        var date = SimpleDateFormat("yyyy-MM-dd").parse(incrementGroup.date)

                        if (date != null && counterWithIncrements.counter.resetType == ResetType.MONTH) date =
                            Date(date.time + (1000 * 60 * 60 * 24))

                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    when {
                                        it == 0 -> stringResource(counterWithIncrements.counter.resetType.headerTitle)
                                        date != null -> DateFormat.format(
                                            DateFormat.getBestDateTimePattern(
                                                Locale.getDefault(),
                                                counterWithIncrements.counter.resetType.headerFormat
                                            ), date
                                        ).toString().replaceFirstChar { it.uppercase() }
                                        else -> "Error"
                                    },
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.padding(24.dp)
                                )
                                Surface(
                                    color = MaterialTheme.colorScheme.surface,
                                    shape = RoundedCornerShape(16.dp),
                                    modifier = Modifier.padding(8.dp),
                                    tonalElevation = 2.dp
                                ) {
                                    Text(
                                        incrementGroup.count.toString(),
                                        style = MaterialTheme.typography.titleLarge,
                                        modifier = Modifier.padding(
                                            horizontal = 24.dp,
                                            vertical = 16.dp
                                        )
                                    )
                                }
                            }
                        }
                        Column {
                            for (increment in counterWithIncrements.increments.reversed().filter {
                                incrementGroup.uids.split(
                                    ','
                                ).contains(it.uid.toString())
                            }) {
                                IncrementEntry(
                                    increment,
                                    countersListViewModel,
                                    counterWithIncrements.counter.resetType
                                )
                                MenuDefaults.Divider()
                            }
                        }
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