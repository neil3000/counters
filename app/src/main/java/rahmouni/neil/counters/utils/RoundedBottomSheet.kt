package rahmouni.neil.counters.utils

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsPadding
import kotlin.math.roundToInt

const val CORNER_RADIUS = 16

@OptIn(ExperimentalMaterialApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun RoundedBottomSheet(
    state: ModalBottomSheetState,
    content: @Composable () -> Unit,
    childContent: @Composable () -> Unit
) {

    val localConfiguration = LocalConfiguration.current
    val context = LocalContext.current

    var reachTop by rememberSaveable { mutableStateOf(false) }

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
        sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        sheetContent = {
            Surface(tonalElevation = 1.dp) {
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .navigationBarsPadding()
                        .onGloballyPositioned { layoutCoordinates ->
                            reachTop =
                                layoutCoordinates.size.height >= localConfiguration.screenHeightDp * context.resources.displayMetrics.density
                        },
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    item {
                        Surface(
                            modifier = Modifier
                                .width(32.dp)
                                .height(4.dp)
                                .align(Alignment.CenterHorizontally),
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(16.dp)
                        ) {}
                    }
                    item {
                        content()
                    }
                }
            }
        },
        sheetShape = RoundedCornerShape(getCornerRadius().dp, getCornerRadius().dp, 0.dp, 0.dp)
    ) {
        childContent()
    }
}
