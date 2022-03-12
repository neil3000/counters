package rahmouni.neil.counters.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.utils.ColorCircle

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CounterStyleOption(
    styleSelected: CounterStyle,
    inModal: Boolean = false,
    onChange: (CounterStyle) -> Unit
) {
    LazyRow(
        Modifier
            .fillMaxWidth()
            .padding(if (inModal) 16.dp else 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(CounterStyle.values().size) {
            ColorCircle(
                counterStyle = CounterStyle.values()[it],
                selected = styleSelected.ordinal == it
            ) {
                onChange(CounterStyle.values()[it])
            }
        }
    }
}