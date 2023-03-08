package rahmouni.neil.counters.counterActivity

import android.content.Intent
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
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
import rahmouni.neil.counters.prefs
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SuggestionsCard(counter: CounterAugmented) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current

    var spawn: Boolean by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        spawn = true
    }

    fun openSettings() {
        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

        context.startActivity(
            Intent(
                context,
                CounterSettingsActivity::class.java
            ).putExtra(
                "counterID",
                counter.uid
            )
        )
    }

    AnimatedVisibility(
        visible = spawn,
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
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface, tonalElevation = 5.dp
        ) {

            Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 8.dp, top = 8.dp, bottom = 6.dp)
                ) {
                    Row(
                        horizontalArrangement = spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Outlined.TipsAndUpdates, null)
                        Text(
                            stringResource(R.string.suggestionsCard_title),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                    Surface(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable { openSettings() }
                    ) {
                        Icon(Icons.Outlined.Close, null, Modifier.scale(.65f))
                    }
                }
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    AssistChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        onClick = {
                            openSettings()
                        },
                        label = { Text("Edit Name") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Title,
                                null,
                                Modifier.size(AssistChipDefaults.IconSize)
                            )
                        },
                    )
                    AssistChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        onClick = {
                            openSettings()
                        },
                        label = { Text("Add entry") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.Add,
                                null,
                                Modifier.size(AssistChipDefaults.IconSize)
                            )
                        },
                    )
                    AssistChip(
                        modifier = Modifier.padding(horizontal = 4.dp),
                        onClick = {
                            openSettings()
                        },
                        label = { Text("Set a goal") },
                        leadingIcon = {
                            Icon(
                                Icons.Outlined.EmojiEvents,
                                null,
                                Modifier.size(AssistChipDefaults.IconSize)
                            )
                        },
                    )
                }
            }
        }
    }
}