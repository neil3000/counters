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
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurfaceDefaults.tonalElevation
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.rebased.Post

@Composable
fun Poll(post: Post) {
    val haptic = getHaptic()

    var votesCount by remember { mutableStateOf(post.additionalInfos.map { 0 }.toMutableList()) }
    var selectedOption by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier.padding(top = 20.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        post.additionalInfos.forEachIndexed { index, info ->
            val totalVotes = votesCount.sum()
            val percentage = if (totalVotes > 0) votesCount[index] * 100 / totalVotes else 0

            Surface(
                modifier = Modifier
                    .fillMaxWidth(),
                tonalElevation = tonalElevation,
                shape = Rn3ExpandableSurfaceDefaults.shape,
                onClick = {
                    selectedOption = info
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
                        text = info,
                    )
                    Text(
                        text = "$percentage%",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    RadioButton(
                        selected = selectedOption == info,
                        onClick = {
                            selectedOption = info
                            haptic.click()
                        },
                    )
                }
            }
        }
    }
}
