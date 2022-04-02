package rahmouni.neil.counters.counter_card.activity

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.Increment
import rahmouni.neil.counters.database.IncrementGroup
import rahmouni.neil.counters.utils.FullscreenDynamicSVG
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
            val incrementGroups: List<IncrementGroup>? by countersListViewModel.getCounterIncrementGroups(
                counter.uid,
                counter.resetType
            ).observeAsState()

            if (incrementGroups != null) {
                LazyColumn(contentPadding = innerPadding) {
                    items(incrementGroups!!) { ig ->
                        val date = LocalDate.parse(
                            ig.date,
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")
                        )

                        Surface(
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(
                                    counter.resetType.format(date)
                                        ?: stringResource(counter.resetType.headerTitle),
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
                                        ig.count.toString(),
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
                            for (increment in increments.filter {
                                ig.uids.split(
                                    ','
                                ).contains(it.uid.toString())
                            }) {
                                IncrementEntry(
                                    increment,
                                    countersListViewModel,
                                    counter.resetType
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