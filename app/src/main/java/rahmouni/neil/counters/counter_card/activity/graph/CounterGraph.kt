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
import androidx.compose.ui.platform.LocalContext
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
import rahmouni.neil.counters.database.IncrementGroup
import rahmouni.neil.counters.utils.FullscreenDynamicSVG
import rahmouni.neil.counters.utils.tiles.TileDialogRadioButtons

@SuppressLint("SimpleDateFormat")
@Composable
fun CounterGraph(
    counter: CounterAugmented?,
    countersListViewModel: CountersListViewModel,
) {
    val context = LocalContext.current
    //val remoteConfig = FirebaseRemoteConfig.getInstance()

    if (counter != null) {
        val incrementGroupsList: List<IncrementGroup> by countersListViewModel.getCounterIncrementGroups(
            counter.uid,
            ResetType.DAY
        ).observeAsState(listOf())

        var groupType: GraphGroupType? by rememberSaveable { mutableStateOf(GraphGroupType.DAY) }

        if (incrementGroupsList.isNotEmpty()) {
            val primaryColor = MaterialTheme.colorScheme.primary.toArgb()
            val onPrimaryColor = MaterialTheme.colorScheme.onPrimary.toArgb()
            val onBackgroundColor = MaterialTheme.colorScheme.onBackground.toArgb()

            LazyColumn {
                item {
                    AndroidView(
                        factory = { ctx ->
                            BarChart(ctx).apply {
                                val entries: ArrayList<BarEntry> = ArrayList()

                                for (i in incrementGroupsList.indices) entries.add(
                                    BarEntry(
                                        i.toFloat(),
                                        incrementGroupsList[i].count.toFloat()
                                    )
                                )

                                val barDataSet = BarDataSet(entries, "")
                                barDataSet.color = primaryColor
                                barDataSet.valueTextColor = onBackgroundColor

                                data = BarData(barDataSet)

                                axisLeft.setDrawGridLines(true)
                                xAxis.setDrawGridLines(false)
                                xAxis.setDrawAxisLine(false)
                                xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
                                xAxis.setDrawLabels(true)
                                xAxis.granularity = 1f
                                xAxis.labelRotationAngle = +90f
                                xAxis.textColor = onPrimaryColor
                                axisLeft.axisLineColor = onBackgroundColor
                                axisLeft.textColor = onBackgroundColor

                                axisRight.isEnabled = false
                                legend.isEnabled = false
                                description.isEnabled = false

                                isDragEnabled = false
                                setScaleEnabled(false)
                                setPinchZoom(false)
                                isDoubleTapToZoomEnabled = false

                                animateXY(1000, 2000, Easing.EaseOutQuart)

                                invalidate()
                            }
                        },
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth()
                            .aspectRatio(1.5f)
                    )
                }

                item {
                    TileDialogRadioButtons(
                        title = "Entries grouping",
                        dialogTitle = "Group entries by",
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