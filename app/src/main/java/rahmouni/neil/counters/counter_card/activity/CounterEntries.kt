package rahmouni.neil.counters.counter_card.activity

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.Increment
import rahmouni.neil.counters.database.IncrementGroup
import rahmouni.neil.counters.utils.FullscreenDynamicSVG
import rahmouni.neil.counters.utils.header.HeaderEndValue

data class EntriesListData(
    val resetType: ResetType,
    val incrementGroupsList: List<IncrementGroup>
) : Serializable

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SimpleDateFormat")
@Composable
fun CounterEntries(
    counter: CounterAugmented?,
    increments: List<Increment>?,
    countersListViewModel: CountersListViewModel,
    alignLeft: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val context = LocalContext.current
    //val remoteConfig = FirebaseRemoteConfig.getInstance()
    val localHapticFeedback = LocalHapticFeedback.current

    if (counter != null && increments?.isNotEmpty() == true) {
        var resetType: ResetType by rememberSaveable { mutableStateOf(counter.resetType) }
        val incrementGroupsList: List<IncrementGroup> by countersListViewModel.getCounterIncrementGroups(
            counter.uid,
            if (resetType == ResetType.NEVER) ResetType.DAY else resetType
        ).observeAsState(
            listOf()
        )
        var entriesListData: EntriesListData by rememberSaveable {
            mutableStateOf(
                EntriesListData(
                    resetType,
                    incrementGroupsList
                )
            )
        }
        var dataReady: Boolean by rememberSaveable { mutableStateOf(false) }

        LaunchedEffect(incrementGroupsList) {
            entriesListData = EntriesListData(resetType, incrementGroupsList)
            dataReady = true
        }

        AnimatedVisibility(visible = dataReady, enter = fadeIn(), exit = fadeOut()) {
            LazyColumn(contentPadding = contentPadding) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp, start = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(
                            8.dp,
                            if (alignLeft) Alignment.Start else Alignment.CenterHorizontally
                        )
                    ) {
                        for (groupType in GroupType.values()) {
                            FilterChip(
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onTertiaryContainer,
                                    selectedLeadingIconColor = MaterialTheme.colorScheme.onTertiaryContainer
                                ),
                                selected = groupType.resetType == resetType,
                                onClick = {
                                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                                    if (groupType.resetType == resetType) {
                                        resetType = ResetType.NEVER
                                        entriesListData =
                                            EntriesListData(ResetType.NEVER, incrementGroupsList)
                                    } else {
                                        if (groupType.resetType != ResetType.DAY) {
                                            dataReady = false
                                        } else {
                                            entriesListData =
                                                EntriesListData(
                                                    groupType.resetType,
                                                    incrementGroupsList
                                                )
                                        }
                                        resetType = groupType.resetType
                                    }
                                },
                                label = { Text(stringResource(groupType.title)) }
                            )
                        }
                    }
                }

                if (entriesListData.resetType.entriesGroup1 == null) {
                    items(increments) { increment ->
                        Box(Modifier.padding(start = 8.dp)) {
                            IncrementEntry(increment, countersListViewModel, counter.valueType)
                        }
                    }
                } else {
                    for (ig in entriesListData.incrementGroupsList) {
                        val date = Calendar.getInstance()
                        date.clear()
                        date.time = SimpleDateFormat("yyyy-MM-dd").parse(ig.date)!!

                        item {
                            HeaderEndValue(
                                title = entriesListData.resetType.format(date, context)
                                    ?: stringResource(entriesListData.resetType.headerTitle)
                            ) {
                                counter.valueType.mediumDisplay(
                                    ig.count + counter.resetValue,
                                    context
                                )
                            }
                        }
                        items(increments.filter {
                            ig.uids.split(
                                ','
                            ).contains(it.uid.toString())
                        }) {
                            Box(Modifier.padding(start = 8.dp)) {
                                IncrementEntry(
                                    it,
                                    countersListViewModel,
                                    counter.valueType,
                                    entriesListData.resetType
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        FullscreenDynamicSVG(
            R.drawable.ic_empty_entries,
            R.string.counterEntries_fdSVG_noEntries
        )
    }
}