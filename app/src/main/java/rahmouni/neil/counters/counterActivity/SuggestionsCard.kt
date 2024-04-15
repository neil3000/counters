package rahmouni.neil.counters.counterActivity

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import rahmouni.neil.counters.R
import rahmouni.neil.counters.goals.ResetType
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel

@Composable
fun SuggestionsCard(
    counter: CounterAugmented,
    countersListViewModel: CountersListViewModel,
    openNewIncrementBottomSheetCallback: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

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

    var dismissed: Boolean by rememberSaveable { mutableStateOf(counter.dismissedSuggestions) }

    val titleSuggestionShown = counter.getDisplayNameOrNull() == null
    val entrySuggestionShown =
        counter.getRawCount(ResetType.NEVER) < 4 && counter.lastIncrement == counter.getCount()
    val goalSuggestionShown = !counter.isGoalEnabled()

    val cardShown =
        !dismissed && (titleSuggestionShown || entrySuggestionShown || goalSuggestionShown)

    AnimatedVisibility(
        visible = cardShown && spawn,
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
                .padding(start = 16.dp, end = 16.dp, top = 16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface, tonalElevation = 5.dp
        ) {

            Column(Modifier.padding(vertical = 8.dp)) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 24.dp, top = 8.dp, bottom = 6.dp, end = 16.dp)
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
                            .clickable {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                countersListViewModel.updateCounter(
                                    counter
                                        .copy(
                                            dismissedSuggestions = true
                                        )
                                        .toCounter()
                                )
                                dismissed = true
                            }
                    ) {
                        Icon(Icons.Outlined.Close, null, Modifier.scale(.65f))
                    }
                }
                Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
                    Spacer(modifier = Modifier.width(16.dp))
                    if (titleSuggestionShown) {
                        AssistChip(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            onClick = {
                                openSettings()
                            },
                            label = { Text(stringResource(id = R.string.suggestionsCard_title_assistChip_label)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Title,
                                    null,
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            },
                        )
                    }
                    AnimatedVisibility(visible = entrySuggestionShown) {
                        AssistChip(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                openNewIncrementBottomSheetCallback()
                            },
                            label = { Text(stringResource(id = R.string.suggestionsCard_entry_assistChip_label)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.Add,
                                    null,
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            },
                        )
                    }
                    if (goalSuggestionShown) {
                        AssistChip(
                            modifier = Modifier.padding(horizontal = 4.dp),
                            onClick = {
                                openSettings()
                            },
                            label = { Text(stringResource(id = R.string.suggestionsCard_goal_assistChip_label)) },
                            leadingIcon = {
                                Icon(
                                    Icons.Outlined.EmojiEvents,
                                    null,
                                    Modifier.size(AssistChipDefaults.IconSize)
                                )
                            },
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}