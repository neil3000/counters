package rahmouni.neil.counters.options

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
//import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.CounterStyle
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.tiles.tile_color_selection.Size
import rahmouni.neil.counters.utils.tiles.tile_color_selection.TileColorSelection

@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
@Composable
fun CounterStyleOption(
    selected: CounterStyle,
    size: Size = Size.BIG,
    onChange: (CounterStyle) -> Unit
) {
    //val remoteConfig = FirebaseRemoteConfig.getInstance()
    val haptic = LocalHapticFeedback.current

    var dynamic by rememberSaveable { mutableStateOf(true) }

    LaunchedEffect(key1 = selected) {
        dynamic = selected.isDynamic
    }

    Column {
        Box {
            listOf(true, false).forEach { bo ->
                this@Column.AnimatedVisibility(
                    dynamic == bo,
                    enter = slideInHorizontally(initialOffsetX = { if (bo) -it else it }),
                    exit = slideOutHorizontally(targetOffsetX = { if (bo) -it else it })
                ) {
                    LazyRow(
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        CounterStyle.getAvailableValues(bo).forEach { cs ->
                            item {
                                TileColorSelection(
                                    color = cs.getBackGroundColor(),
                                    selected = selected == cs,
                                    size = size
                                ) {
                                    onChange(cs)
                                }
                            }
                        }
                    }
                }
            }
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(8.dp),
            Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
        ) {
            // DynamicColors
            FilterChip(
                selected = dynamic,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                    dynamic = true
                },
                label = { Text(stringResource(R.string.counterStyleOption_filterChip_dynamicColors_label)) },
            )

            // BasicColors
            FilterChip(
                selected = !dynamic,
                onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)

                    dynamic = false
                },
                label = { Text(stringResource(R.string.counterStyleOption_filterChip_basicColors_label)) },
            )
        }
    }
}