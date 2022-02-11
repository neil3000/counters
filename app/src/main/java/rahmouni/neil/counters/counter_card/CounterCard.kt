package rahmouni.neil.counters.counter_card

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import rahmouni.neil.counters.Counter
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.ui.theme.CountersTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterCard(data: Counter) {
    val interactionSource = remember { MutableInteractionSource() }

    val context = LocalContext.current
    val localHapticFeedback = LocalHapticFeedback.current

    Card(
        interactionSource = interactionSource,
        containerColor = data.style.getBackGroundColor(),
        modifier = Modifier.clickable(
            interactionSource = interactionSource,
            indication = null,
            onClick = {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                context.startActivity(
                    Intent(context, CounterActivity::class.java).putExtra(
                        "counter",
                        Gson().toJson(data)
                    )
                )
            } //TODO
        )
    ) {
        Column {
            Text(
                text = data.displayName,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(all = 8.dp)
            )
            CounterCardButtons(data)
        }
    }

}

@Preview(showBackground = true, widthDp = 150)
@Composable
fun CounterCardPreview() {
    CountersTheme {
        CounterCard(Counter(1, "Mollets", CounterStyle.SECONDARY, false))
    }
}

@Preview(showBackground = true, widthDp = 150)
@Composable
fun CounterCardPreviewMinus() {
    CountersTheme {
        CounterCard(Counter(1, "Mollets", CounterStyle.SECONDARY, true))
    }
}