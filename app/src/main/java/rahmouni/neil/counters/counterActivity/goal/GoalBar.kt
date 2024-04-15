package rahmouni.neil.counters.counterActivity.goal

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.EmojiEvents
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.prefs
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
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    var spawn: Boolean by rememberSaveable { mutableStateOf(false) }
    var height: Dp by remember { mutableStateOf(0.dp) }

    LaunchedEffect(Unit) {
        spawn = true
    }

    val animatedProgress = animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "AnimatedProgress"
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

    AnimatedVisibility(
        visible = spawn,
        modifier = modifier,
        enter = fadeIn(
            tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing,
                delayMillis = 300
            )
        ) + scaleIn(
            tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing,
                delayMillis = 300
            ), initialScale = .95f
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged { height = with(localDensity) { it.height.toDp() } },
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface, tonalElevation = 1.dp
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
                    if (remoteConfig.getBoolean("i_270")) {
                        LinearProgressIndicator(
                            progress = { animatedProgress.value },
                            modifier = Modifier
                                .height(8.dp)
                                .semantics(mergeDescendants = true) {},
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        LinearProgressIndicator(
                            progress = animatedProgress.value,
                            modifier = Modifier
                                .height(8.dp)
                                .semantics(mergeDescendants = true) {}
                        )
                    }
                }
            }
            AnimatedVisibility(
                visible = animatedProgress.value >= 1 || currentProgress >= 1,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        Modifier
                            .padding(horizontal = 8.dp)
                            .height(height),
                        horizontalArrangement = spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.EmojiEvents, null,
                            Modifier
                                .scale(1.2f)
                                .padding(start = 12.dp)
                        )
                        Text(
                            stringResource(R.string.goalBar_achieved_title),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.weight(1f)
                        )
                        Button(onClick = { openSettings() }, Modifier.scale(.875f)) {
                            Text(stringResource(R.string.goalBar_achieved_button))
                        }
                    }
                    if (!prefs.confettiDisabled) {
                        KonfettiView(
                            Modifier
                                .height(height)
                                .fillMaxWidth(),
                            parties = listOf(party)
                        )
                    }
                }
            }
        }
    }
}