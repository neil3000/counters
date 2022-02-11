package rahmouni.neil.counters.new_counter.color_selector

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.ui.theme.CountersTheme

@Composable
fun CounterStyleSelector(styleSelected: CounterStyle, onChange: (CounterStyle) -> Unit) {
    LazyRow(
        Modifier.fillMaxWidth(),
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

@Preview(showBackground = true)
@Composable
fun CounterStyleSelectorPreview() {
    CountersTheme {
        CounterStyleSelector(CounterStyle.DEFAULT) {}
    }
}