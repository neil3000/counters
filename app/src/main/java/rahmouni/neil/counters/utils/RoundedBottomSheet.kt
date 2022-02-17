package rahmouni.neil.counters

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.*
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.navigationBarsWithImePadding
import kotlin.math.roundToInt

const val CORNER_RADIUS = 16

@OptIn(ExperimentalMaterialApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun RoundedBottomSheet(
    state: ModalBottomSheetState,
    reachTop: Boolean,
    content: @Composable () -> Unit,
    childContent: @Composable () -> Unit
) {
    fun getCornerRadius(): Int {
        if (reachTop) {
            if (state.currentValue == ModalBottomSheetValue.Expanded && state.progress.to == ModalBottomSheetValue.HalfExpanded) {
                return (state.progress.fraction.coerceAtMost(.2f) * 5 * CORNER_RADIUS).roundToInt()
            } else if (state.targetValue == ModalBottomSheetValue.Expanded) {
                return CORNER_RADIUS - ((state.progress.fraction.coerceAtLeast(.8f) - .8) * 5 * CORNER_RADIUS).roundToInt()
            }
        }
        return CORNER_RADIUS
    }

    ModalBottomSheetLayout(
        sheetState = state,
        sheetBackgroundColor = MaterialTheme.colorScheme.background,
        sheetElevation = 0.dp,
        sheetContent = {
            Column(
                Modifier
                    .padding(top = 10.dp)
                    .navigationBarsPadding(),
                Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .width(32.dp)
                        .height(4.dp)
                        .align(Alignment.CenterHorizontally),
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = RoundedCornerShape(16.dp)
                ) {}
                content()
            }
        },
        sheetShape = RoundedCornerShape(getCornerRadius().dp, getCornerRadius().dp, 0.dp, 0.dp)
    ) {
        childContent()
    }
}
