package rahmouni.neil.counters.counterActivity.goal

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterAugmented
import java.util.concurrent.TimeUnit

@Composable
fun GoalBar(
    currentProgress: Float,
    counter: CounterAugmented,
    modifier: Modifier
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val localDensity = LocalDensity.current

    var spawn: Boolean by rememberSaveable { mutableStateOf(false) }
    var height: Dp by remember { mutableStateOf(0.dp) }

    LaunchedEffect(Unit) {
        spawn = true
    }

    val animatedProgress = animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )

    val party = Party(
        speed = 0f,
        maxSpeed = 50f,
        damping = 0.9f,
        spread = 1,
        colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
        emitter = Emitter(duration = 1, TimeUnit.DAYS).perSecond(8),
        position = Position.Relative(-0.2, -0.2)
    )

    fun openSettings() {
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

    // Title
    AnimatedVisibility(
        visible = spawn,
        modifier = modifier,
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
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { height = with(localDensity) { it.height.toDp() } },
            shape = RoundedCornerShape(24.dp)
        ) {
            AnimatedVisibility(
                visible = animatedProgress.value >= 1,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.EmojiEvents, null,
                            Modifier
                                .scale(1.2f)
                                .padding(start = 12.dp))
                        Text(
                            stringResource(R.string.goalBar_achieved),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        Button(onClick = { openSettings() }, Modifier.scale(.875f)) {
                            Text("Increase target")
                        }
                    }
                    KonfettiView(
                        Modifier
                            .height(height)
                            .fillMaxWidth(),
                        parties = listOf(party)
                    )
                }
            }

            AnimatedVisibility(
                visible = animatedProgress.value < 1,
                enter = fadeIn(),
                exit = fadeOut()
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
                                .clickable { openSettings() }
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
}