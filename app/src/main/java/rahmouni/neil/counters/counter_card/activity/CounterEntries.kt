package rahmouni.neil.counters.counter_card.activity

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.Increment
import rahmouni.neil.counters.utils.FullscreenDynamicSVG

@SuppressLint("SimpleDateFormat")
@Composable
fun CounterEntries(
    counter: CounterAugmented?,
    increments: List<Increment>?,
    countersListViewModel: CountersListViewModel,
    innerPadding: PaddingValues
) {
    if (counter != null && increments?.isNotEmpty() == true) {
        if (counter.resetType.entriesGroup1 == null) {
            LazyColumn(contentPadding = innerPadding) {
                items(increments) { increment ->
                    IncrementEntry(increment, countersListViewModel)
                    MenuDefaults.Divider()
                }
            }
        } else {
            /*val incrementGroups: List<IncrementGroup>? by countersListViewModel.getCounterIncrementGroups(
                counter.uid,
                counter.resetType
            ).observeAsState()

            if (incrementGroups != null) {
                LazyColumn(contentPadding = innerPadding) {
                    itemsIndexed(incrementGroups!!) { it, incrementGroup ->
                        var date = SimpleDateFormat("yyyy-MM-dd").parse(incrementGroup.date)

                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    if (date != null) {
                                        if (it == 0 && (Date().time - date.time)/(1000*60*60) < counterWithIncrements.counter.resetType.millisGroup)
                                            stringResource(counterWithIncrements.counter.resetType.headerTitle)
                                        else DateFormat.format(
                                            DateFormat.getBestDateTimePattern(
                                                Locale.getDefault(),
                                                counterWithIncrements.counter.resetType.headerFormat
                                            ), date
                                        ).toString().replaceFirstChar { it.uppercase() }
                                    } else "Error",
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
            }*/
        }
    } else {
        FullscreenDynamicSVG(
            R.drawable.ic_empty_entries,
            R.string.text_noEntriesYet
        )
    }
}