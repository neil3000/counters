package dev.rahmouni.neil.counters.feature.dashboard.newCounter.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.shapes.Rn3Shapes
import dev.rahmouni.neil.counters.core.shapes.Shape

@Composable
internal fun Step(number: Int, name: String) {
    Row(Modifier.padding(horizontal = 4.dp, vertical = 12.dp), Arrangement.spacedBy(8.dp), Alignment.CenterVertically) {
        Box(
            Modifier
                .size(54.dp)
                .clip(Shape(Rn3Shapes.Clover))
                .background(MaterialTheme.colorScheme.tertiaryContainer),
        ) {
            Text(
                number.toString(),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center),
            )
        }
        Text(name, style = MaterialTheme.typography.titleLarge)
    }
}