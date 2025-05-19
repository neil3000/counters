package dev.rahmouni.neil.counters.feature.dashboard.newCounter.ui

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.feature.dashboard.newCounter.CounterCategory

@Composable
internal fun PickingCategoryPanel(paddingValues: Rn3PaddingValues) {
    Column(Modifier.padding(paddingValues.add(horizontal = 6.dp))) {
        Step(number = 1, name = "Select a preset")

        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 165.dp)) {
            items(CounterCategory.entries, key = { it.ordinal }) {
                with(it) {
                    Card(
                        onClick = { /*TODO*/ },
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondaryContainer),
                        modifier = Modifier
                            .padding(6.dp)
                            .aspectRatio(1f),
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalArrangement = spacedBy(8.dp),
                        ) {
                            Surface(
                                Modifier.size(48.dp),
                                color = MaterialTheme.colorScheme.surface,
                                shape = CircleShape,
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxSize(),
                                    tint = MaterialTheme.colorScheme.secondary,
                                )
                            }
                            Text(
                                text = displayName,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(
                                Icons.Outlined.ChevronRight, "Continue with this preset",
                                modifier = Modifier.align(Alignment.End),
                            )
                        }
                    }
                }
            }
        }
    }
}