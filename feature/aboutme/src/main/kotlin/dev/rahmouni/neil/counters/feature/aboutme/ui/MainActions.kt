package dev.rahmouni.neil.counters.feature.aboutme.ui

import androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi
import androidx.compose.animation.graphics.res.animatedVectorResource
import androidx.compose.animation.graphics.res.rememberAnimatedVectorPainter
import androidx.compose.animation.graphics.vector.AnimatedImageVector
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.R
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClickChips
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import kotlinx.coroutines.delay

@OptIn(ExperimentalAnimationGraphicsApi::class)
@Composable
fun MainActions() {
    // TODO all the logic...

    Surface(
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
    ) {
        var atEndAnimatedCoffeeIcon by remember { mutableStateOf(false) }
        val coffeeIcon = rememberAnimatedVectorPainter(
            animatedImageVector = AnimatedImageVector.animatedVectorResource(id = R.drawable.core_designsystem_icon_coffee_animated),
            atEnd = false,
        )
        val animatedCoffeeIcon = rememberAnimatedVectorPainter(
            animatedImageVector = AnimatedImageVector.animatedVectorResource(id = R.drawable.core_designsystem_icon_coffee_animated),
            atEnd = atEndAnimatedCoffeeIcon,
        )

        LaunchedEffect(Unit) {
            while (true) {
                atEndAnimatedCoffeeIcon = !atEndAnimatedCoffeeIcon
                delay(1000) //animDuration
                atEndAnimatedCoffeeIcon = !atEndAnimatedCoffeeIcon
                delay((1000..2000).random().toLong())
            }
        }

        // TODO all the logic...
        Column {
            Rn3TileClick(
                title = "Portfolio",
                icon = Icons.Outlined.Language,
                supportingText = "Check out my other work \uD83D\uDC22❤\uFE0F",
                external = true,
            ) {}
            Rn3TileHorizontalDivider(color = MaterialTheme.colorScheme.secondary)
            Rn3TileClickChips(
                title = "Buy me a coffee",
                icon = if (atEndAnimatedCoffeeIcon) animatedCoffeeIcon else coffeeIcon,
                onClick = {},
            ) {
                items(listOf("1 €", "3 €", "5 €", "Custom")) {
                    FilterChip(
                        selected = it == "3 €",
                        onClick = { /* Do something! */ },
                        label = { Text(it) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = MaterialTheme.colorScheme.tertiary,
                            selectedLabelColor = MaterialTheme.colorScheme.onTertiary,
                        ),
                    )
                }
            }
        }
    }
}