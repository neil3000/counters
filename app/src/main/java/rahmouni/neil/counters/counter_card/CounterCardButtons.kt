package rahmouni.neil.counters.counter_card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import rahmouni.neil.counters.Counter
import rahmouni.neil.counters.Increment
import rahmouni.neil.counters.counterDao
import rahmouni.neil.counters.ui.theme.CountersTheme

@Composable
fun CounterCardButtons(data: Counter) {
    if (data.hasMinus) return CounterCardButtonsMinus(data)
    return CounterCardButtonsDefault(data)
}

@Composable
fun CounterCardButtonsDefault(data: Counter) {
    val coroutineScope = rememberCoroutineScope()
    val localHapticFeedback = LocalHapticFeedback.current

    var count by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        count = counterDao?.getCount(data.uid) ?: 0
    }

    fun increment(step: Int) = run {
        coroutineScope.launch {
            counterDao?.addIncrement(Increment(counterID = data.uid, value = step))
            count = counterDao?.getCount(data.uid) ?: -1
            localHapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    Row(
        horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp)
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineLarge,
        )
        IconButton(onClick = { increment(data.incrementValue) }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add", //TODO i18n
            )
        }
    }
}

@Composable
fun CounterCardButtonsMinus(data: Counter) {
    val coroutineScope = rememberCoroutineScope()
    val localHapticFeedback = LocalHapticFeedback.current

    var count by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        count = counterDao?.getCount(data.uid) ?: 0
    }

    fun increment(step: Int) = run {
        coroutineScope.launch {
            counterDao?.addIncrement(Increment(counterID = data.uid, value = step))
            count = counterDao?.getCount(data.uid) ?: -1
            localHapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
        }
    }

    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
        IconButton(onClick = { increment(-data.incrementValue) }) {
            Icon(
                imageVector = Icons.Filled.Remove,
                contentDescription = "Remove", //TODO i18n
            )
        }
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.headlineLarge,
        )
        IconButton(onClick = { increment(data.incrementValue) }) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = "Add", //TODO i18n
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 150)
@Composable
fun CounterCardButtonsDefaultPreview() {
    CountersTheme {
        CounterCardButtons(Counter(0, "Mollets"))
    }
}

@Preview(showBackground = true, widthDp = 150)
@Composable
fun CounterCardButtonsMinusPreview() {
    CountersTheme {
        CounterCardButtons(Counter(0, "Mollets", hasMinus = true))
    }
}