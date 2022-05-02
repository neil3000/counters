package rahmouni.neil.counters.counter_card.activity

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.Increment
import rahmouni.neil.counters.database.IncrementGroup
import rahmouni.neil.counters.utils.*
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun CounterEntries(
    counter: CounterAugmented?,
    increments: List<Increment>?,
    countersListViewModel: CountersListViewModel,
) {
    val context = LocalContext.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    if (counter != null && increments?.isNotEmpty() == true) {
        var resetType: ResetType by rememberSaveable { mutableStateOf(counter.resetType) }
        val incrementGroupsList: List<IncrementGroup> by countersListViewModel.getCounterIncrementGroups(
            counter.uid,
            if (resetType == ResetType.NEVER) ResetType.DAY else resetType
        ).observeAsState(
            listOf()
        )

        LazyColumn {
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(
                        8.dp,
                        Alignment.CenterHorizontally
                    )
                ) {
                    for (groupType in GroupType.values()) {
                        if (remoteConfig.getBoolean("issue71__new_selectable_chips")) {
                            SelectableChipExperiment(
                                text = stringResource(groupType.title),
                                selected = groupType.resetType == resetType,
                                onUnselected = { resetType = ResetType.NEVER }) {
                                resetType = groupType.resetType
                            }
                        } else {
                            SelectableChip(
                                text = stringResource(groupType.title),
                                selected = groupType.resetType == resetType,
                                onUnselected = { resetType = ResetType.NEVER }) {
                                resetType = groupType.resetType
                            }
                        }
                    }
                }
            }

            if (resetType.entriesGroup1 == null) {
                items(increments) { increment ->
                    IncrementEntry(increment, countersListViewModel)
                    MenuDefaults.Divider()
                }
            } else {
                for (ig in incrementGroupsList) {
                    val date = Calendar.getInstance()
                    date.clear()
                    date.time = SimpleDateFormat("yyyy-MM-dd").parse(ig.date)!!

                    item {
                        if (remoteConfig.getBoolean("issue65__header_fix")) {
                            HeaderExperiment(
                                title = resetType.format(date, context)
                                    ?: stringResource(resetType.headerTitle), (ig.count+counter.resetValue).toString()
                            )
                        }else{
                            Header(
                                title = resetType.format(date, context)
                                    ?: stringResource(resetType.headerTitle), (ig.count+counter.resetValue).toString()
                            )
                        }
                    }
                    items(increments.filter {
                        ig.uids.split(
                            ','
                        ).contains(it.uid.toString())
                    }) {
                        IncrementEntry(
                            it,
                            countersListViewModel,
                            resetType
                        )
                        MenuDefaults.Divider()
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