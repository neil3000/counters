package rahmouni.neil.counters.utils.tiles.tile_color_selection

import androidx.compose.animation.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp

@OptIn(
    ExperimentalAnimationApi::class
)
@Composable
fun TileColorSelection(
    color: Color,
    selected: Boolean,
    size: Size = Size.BIG,
    onSelection: () -> Unit
) {
    val localHapticFeedback = LocalHapticFeedback.current
    //val remoteConfig = FirebaseRemoteConfig.getInstance()

    Surface(
        tonalElevation = -LocalAbsoluteTonalElevation.current,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(4.dp)
    ) {
        Surface(Modifier.toggleable(value = selected) {
            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

            onSelection()
        }) {
            Surface(
                color = color,
                shape = CircleShape,
                modifier = Modifier
                    .padding(size.circlePadding)
                    .size(size.circleSize)
            )
            {
                AnimatedVisibility(
                    visible = selected,
                    enter = scaleIn() + fadeIn(),
                    exit = scaleOut() + fadeOut()
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape,
                        modifier = Modifier.padding(size.selectedPadding)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = null,
                            Modifier.requiredSize(size.iconSize)
                        )
                    }
                }
            }
        }
    }
}