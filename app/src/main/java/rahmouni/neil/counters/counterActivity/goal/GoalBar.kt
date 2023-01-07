package rahmouni.neil.counters.counterActivity.goal

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.IncrementGroup
import kotlin.math.min

@Composable
fun GoalBar(
    countersListViewModel: CountersListViewModel,
    counter: CounterAugmented,
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    var spawn: Boolean by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        spawn = true
    }

    val ig: List<IncrementGroup> by countersListViewModel.getCounterIncrementGroups(
        counter.uid,
        counter.goalReset ?: counter.resetType
    ).observeAsState(listOf())

    val currentProgress =
        min(
            ((if (ig.isEmpty()) 0 else ig[0].count) + counter.resetValue).toFloat() / (counter.goalValue
                ?: 1),
            1f
        )
    val animatedProgress = animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )

    // Title
    AnimatedVisibility(
        visible = spawn,
        enter = fadeIn(
            tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing,
                delayMillis = 250
            )
        )
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 1.dp,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                Modifier.padding(start = 24.dp, top = 12.dp, bottom = 16.dp, end = 16.dp),
                verticalArrangement = spacedBy(16.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        horizontalArrangement = spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.EmojiEvents, null)
                        Text(
                            stringResource(R.string.goalBar_title),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.secondary,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                context.startActivity(
                                    Intent(
                                        context,
                                        CounterGoalSettingsActivity::class.java
                                    ).putExtra(
                                        "counterID",
                                        counter.uid
                                    )
                                )
                            }
                    ) {
                        Icon(Icons.Outlined.Edit, null, Modifier.scale(.65f))
                    }
                }
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    LinearProgressIndicator(
                        progress = animatedProgress.value,
                        modifier = Modifier
                            .height(8.dp)
                            .semantics(mergeDescendants = true) {}
                    )
                }
            }
        }
    }
}