package rahmouni.neil.counters.counter_card.activity.graph

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GroupWork
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ResetType
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.Increment
import rahmouni.neil.counters.database.IncrementGroup
import rahmouni.neil.counters.utils.FullscreenDynamicSVG
import rahmouni.neil.counters.utils.tiles.TileDialogRadioButtons
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun CounterGraph(
    counter: CounterAugmented?,
    increments: List<Increment>?,
    countersListViewModel: CountersListViewModel,
) {
    //val remoteConfig = FirebaseRemoteConfig.getInstance()

    if (counter != null && increments?.isNotEmpty() == true) {
        var groupType: GraphGroupType by rememberSaveable { mutableStateOf(GraphGroupType.DAY) }

        val incrementGroupsList: List<IncrementGroup> by countersListViewModel.getCounterIncrementGroups(
            counter.uid,
            if (groupType.resetType == ResetType.NEVER) ResetType.DAY else groupType.resetType
        ).observeAsState(listOf())

        if (incrementGroupsList.isNotEmpty()) {
            val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
            val onPrimaryColor = MaterialTheme.colorScheme.onPrimary.toArgb()
            val onBackgroundColor = MaterialTheme.colorScheme.onBackground.toArgb()

            LazyColumn {
                item {
                    AndroidView(
                        factory = { ctx ->
                            BarChart(ctx).apply {
                                axisLeft.setDrawGridLines(true)
                                xAxis.setDrawGridLines(false)
                                xAxis.setDrawAxisLine(false)
                                xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
                                xAxis.setDrawLabels(true)
                                xAxis.granularity = 1f
                                xAxis.labelRotationAngle = +90f
                                xAxis.textColor = onPrimaryColor
                                xAxis.setDrawLabels(false) //TODO remove
                                axisLeft.axisLineColor = onBackgroundColor
                                axisLeft.textColor = onBackgroundColor

                                axisRight.isEnabled = false
                                legend.isEnabled = false
                                description.isEnabled = false

                                isDragEnabled = false
                                setScaleEnabled(false)
                                setPinchZoom(false)
                                isDoubleTapToZoomEnabled = false
                            }
                        },
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .aspectRatio(1.5f),
                        update = {
                            it.apply {
                                val list = incrementGroupsList.toMutableList()
                                var i = 0
                                clear()

                                val entries: ArrayList<BarEntry> = ArrayList()

                                if (groupType.resetType == ResetType.NEVER) {
                                    for (inc in increments) {
                                        entries.add(
                                            0,
                                            BarEntry(
                                                -i.toFloat(),
                                                (inc.value + counter.resetValue).toFloat()
                                            )
                                        )
                                        i++
                                    }
                                } else {
                                    while (list.isNotEmpty() && i < 1000) {
                                        val cal = Calendar.getInstance()
                                        cal.set(Calendar.HOUR_OF_DAY, 0)
                                        cal.set(Calendar.MINUTE, 0)
                                        cal.set(Calendar.SECOND, 0)
                                        cal.set(Calendar.MILLISECOND, 0)

                                        groupType.offset(cal, i)

                                        val gDate = Calendar.getInstance()
                                        gDate.clear()
                                        gDate.set(Calendar.HOUR_OF_DAY, 1)
                                        gDate.time =
                                            SimpleDateFormat("yyyy-MM-dd").parse(list.first().date)!!

                                        var value = counter.resetValue
                                        if (gDate.equals(cal)) {
                                            value += list.first().count
                                            list.removeFirst()
                                        }

                                        entries.add(
                                            0,
                                            BarEntry(
                                                -i.toFloat(),
                                                value.toFloat()
                                            )
                                        )

                                        i++
                                    }
                                }

                                val barDataSet = BarDataSet(entries, "")
                                barDataSet.color = primaryColor
                                barDataSet.valueTextColor = onBackgroundColor

                                data = BarData(barDataSet)

                                animateXY(1000, 2000, Easing.EaseOutQuart)

                                notifyDataSetChanged()
                                invalidate()
                            }
                        }
                    )
                }

                item {
                    TileDialogRadioButtons(
                        title = stringResource(R.string.text_entriesGrouping),
                        dialogTitle = stringResource(R.string.action_groupEntriesBy),
                        icon = Icons.Outlined.GroupWork,
                        values = GraphGroupType.values().asList(),
                        selected = groupType
                    ) {
                        groupType = it as GraphGroupType
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