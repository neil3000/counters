package rahmouni.neil.counters.counter_card.activity

import android.annotation.SuppressLint
import android.text.format.DateUtils
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.IncrementGroup
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun IncrementGroupEntry(
    incrementGroup: IncrementGroup,
    countersListViewModel: CountersListViewModel
) {
    val date: Date? = SimpleDateFormat("yyyy-MM-dd").parse(incrementGroup.date)

    Text(
        if (date != null) DateUtils.getRelativeTimeSpanString(date.time)
            .toString() else "Error"
    )
}