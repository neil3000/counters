package rahmouni.neil.counters.counterActivity

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement.SpaceBetween
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R
import rahmouni.neil.counters.counter_card.activity.IncrementEntry
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.Increment
import java.lang.Integer.min

@Composable
fun LatestEntries(
    increments: List<Increment>,
    countersListViewModel: CountersListViewModel,
    counter: CounterAugmented,
    rowCount: Int
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    var spawn: Boolean by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        spawn = true
    }

    Column(
        verticalArrangement = spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Title
        AnimatedVisibility(
            visible = spawn,
            enter = fadeIn(
                tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing,
                    delayMillis = 350
                )
            )
        ) {
            Surface(
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(
                    topStart = 24.dp,
                    topEnd = 24.dp,
                    bottomStart = 8.dp,
                    bottomEnd = 8.dp
                )
            ) {
                Surface(Modifier.clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                    context.startActivity(
                        Intent(context, CounterEntriesActivity::class.java).putExtra(
                            "counterID",
                            counter.uid
                        )
                    )
                }) {
                    Row(
                        horizontalArrangement = SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                    ) {
                        Row(
                            horizontalArrangement = spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.AutoMirrored.Outlined.List, null)
                            Text(
                                stringResource(R.string.latestEntries_title),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 2.dp)
                            )
                        }
                        Icon(Icons.AutoMirrored.Outlined.ArrowForward, null)
                    }
                }
            }
        }


        increments.take(rowCount)
            .forEachIndexed { index, increment ->
                val isLast = index == min(rowCount, increments.size) - 1

                AnimatedVisibility(
                    visible = spawn,
                    enter = fadeIn(
                        tween(
                            durationMillis = 400,
                            easing = FastOutSlowInEasing,
                            delayMillis = 350
                        )
                    )
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 1.dp,
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomStart = if (isLast) 24.dp else 8.dp,
                            bottomEnd = if (isLast) 24.dp else 8.dp,
                        )
                    ) {
                        Box(Modifier.padding(bottom = if (isLast) 2.dp else 0.dp)) {
                            IncrementEntry(
                                increment,
                                countersListViewModel,
                                counter.valueType,
                                counter.resetType
                            )
                        }
                    }
                }
            }
    }
}