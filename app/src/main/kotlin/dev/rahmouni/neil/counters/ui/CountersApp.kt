package dev.rahmouni.neil.counters.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.rahmouni.neil.counters.CountersNavHost

@Composable
fun CountersApp(
    appState: CountersAppState,
    modifier: Modifier = Modifier,
) {
    CountersNavHost(appState = appState, modifier)
}