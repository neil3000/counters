package rahmouni.neil.counters.counterActivity.statCount

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StatCount(
    counter: CounterAugmented,
    countersListViewModel: CountersListViewModel,
    type: StatCountType
) {
    val context = LocalContext.current
    val count = type.getCount(counter, countersListViewModel)

    AnimatedVisibility(
        visible = count != null,
        enter = scaleIn(
            tween(
                durationMillis = 400,
                easing = FastOutSlowInEasing,
                delayMillis = 200
            ), initialScale = .8f
        ) + fadeIn(tween(durationMillis = 400, easing = FastOutSlowInEasing, delayMillis = 200))
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(
                    modifier = Modifier.size(64.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    counter.valueType.mediumDisplay(count ?: 0, context)
                }
            }
            Text(
                type.displayName,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}