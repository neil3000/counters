package dev.rahmouni.neil.counters.feature.feed.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.TOP
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.feature.feed.model.PostEntity

@Composable
fun Poll(post: PostEntity) {
    val haptic = getHaptic()

    var votesCount by remember { mutableStateOf(post.additionalInfos.map { 0 }.toMutableList()) }
    var selectedOption by remember { mutableStateOf<String?>(value = null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.padding(Rn3SurfaceDefaults.paddingValues.only(TOP)),
    ) {
        post.additionalInfos.forEachIndexed { index, info ->
            val totalVotes = votesCount.sum()
            val percentage = if (totalVotes > 0) votesCount[index] * 100 / totalVotes else 0

            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                tonalElevation = Rn3SurfaceDefaults.tonalElevation,
                shape = Rn3SurfaceDefaults.shape,
                onClick = {
                    selectedOption = info.first
                    votesCount[index] += 1
                    haptic.click()
                },
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = info.first,
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "$percentage%",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        RadioButton(
                            selected = selectedOption == info.first,
                            onClick = {
                                selectedOption = info.first
                                haptic.click()
                            },
                        )
                    }
                }
            }
        }
    }
}
