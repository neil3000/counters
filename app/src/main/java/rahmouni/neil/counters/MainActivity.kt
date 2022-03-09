package rahmouni.neil.counters

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.LocalWindowInsets
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.rememberInsetsPaddingValues
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import rahmouni.neil.counters.counter_card.CounterCard
import rahmouni.neil.counters.counter_card.NewIncrement
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.new_counter.NewCounter
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.RoundedBottomSheet

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        val countersListViewModel by viewModels<CountersListViewModel> {
            CountersListViewModelFactory((this.applicationContext as CountersApplication).countersListRepository)
        }

        setContent {
            CountersTheme {
                ProvideWindowInsets {
                    androidx.compose.material.Surface {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            Home(countersListViewModel)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(
    ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class,
    androidx.compose.material.ExperimentalMaterialApi::class
)
@Composable
fun Home(countersListViewModel: CountersListViewModel) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val localHapticFeedback = LocalHapticFeedback.current

    val countersList: List<CounterAugmented> by countersListViewModel.allCounters.observeAsState(
        listOf()
    )
    val bottomSheetNewCounterState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val bottomSheetNewIncrementState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    var bottomSheetNewIncrementCounterID: Int? by rememberSaveable { mutableStateOf(null) }

    RoundedBottomSheet(bottomSheetNewIncrementState, {
        NewIncrement(
            counter = if (bottomSheetNewIncrementCounterID == null || countersList.isEmpty()) null else countersList.find { it.uid == bottomSheetNewIncrementCounterID },
            countersListViewModel = countersListViewModel
        ) {
            scope.launch {
                bottomSheetNewIncrementState.hide()
                bottomSheetNewIncrementCounterID = null
            }

        }
    }) {
        RoundedBottomSheet(bottomSheetNewCounterState, {
            NewCounter(countersListViewModel) {
                scope.launch {
                    bottomSheetNewCounterState.hide()
                }
            }
        }) {
            Scaffold(
                modifier = Modifier
                    .nestedScroll(scrollBehavior.nestedScrollConnection)
                    .statusBarsPadding(),
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(stringResource(R.string.text_appName))
                        },
                        actions = {
                            IconButton(onClick = {
                                context.startActivity(
                                    Intent(context, SettingsActivity::class.java)
                                )
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.MoreVert,
                                    contentDescription = stringResource(R.string.text_settings)
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text(stringResource(R.string.action_newCounter_short)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            scope.launch {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                bottomSheetNewCounterState.animateTo(ModalBottomSheetValue.Expanded)
                            }
                        },
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            ) {
                if (countersList.isNotEmpty()) {
                    LazyVerticalGrid(
                        cells = GridCells.Adaptive(minSize = 180.dp),
                        contentPadding = rememberInsetsPaddingValues(
                            insets = LocalWindowInsets.current.navigationBars,
                            applyBottom = true,
                            additionalStart = 8.dp,
                            additionalEnd = 8.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(countersList) { counter ->
                            CounterCard(counter, countersListViewModel) {
                                bottomSheetNewIncrementCounterID = counter.uid
                                scope.launch {
                                    bottomSheetNewIncrementState.show()
                                }
                            }
                        }
                    }
                } else {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_balloons),
                            null,
                            Modifier
                                .fillMaxWidth(.5f),
                            Color.Unspecified
                        )
                        Text(
                            stringResource(R.string.text_noCountersYet),
                            Modifier.padding(top = 24.dp),
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }
        }
    }
}

/*@Preview(showBackground = true)
@Composable
fun HomePreview() {
    CountersTheme {
        Home()
    }
}*/