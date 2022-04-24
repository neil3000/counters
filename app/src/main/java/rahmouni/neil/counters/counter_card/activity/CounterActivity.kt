package rahmouni.neil.counters.counter_card.activity

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsPadding
import kotlinx.coroutines.launch
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.R
import rahmouni.neil.counters.counter_card.NewIncrement
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.database.Increment
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.RoundedBottomSheet
import rahmouni.neil.counters.utils.SettingsDots

class CounterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val counterID: Int = intent.getIntExtra("counterID", 0)

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
                            CounterPage(
                                counterID, countersListViewModel
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class,
    ExperimentalMaterial3WindowSizeClassApi::class, ExperimentalAnimationApi::class
)
@Composable
fun CounterPage(counterID: Int, countersListViewModel: CountersListViewModel) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    val navController = rememberNavController()
    val windowSize = calculateWindowSizeClass(activity = activity)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val counter: CounterAugmented? by countersListViewModel.getCounter(counterID).observeAsState()
    val increments: List<Increment>? by countersListViewModel.getCounterIncrements(counterID)
        .observeAsState()

    RoundedBottomSheet(bottomSheetState, {
        if (counter != null) {
            NewIncrement(counter, countersListViewModel) {
                scope.launch {
                    bottomSheetState.hide()
                }
            }
        }
    }) {
        Scaffold(
            modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .statusBarsPadding(),
            topBar = {
                SmallTopAppBar(
                    title = { Text(counter?.displayName ?: "Counter") },
                    actions = { SettingsDots {} },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                activity.finish()
                            },
                            modifier = Modifier.testTag("BACK_ARROW")
                        ) {
                            Icon(
                                Icons.Outlined.ArrowBack,
                                contentDescription = stringResource(R.string.action_back_short)
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                AnimatedVisibility(
                    currentDestination?.route == "entries",
                    enter = slideInVertically{ 200 },
                    exit = slideOutVertically{ 200 }
                ) {
                    if (windowSize.widthSizeClass == WindowWidthSizeClass.Compact) {
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                scope.launch {
                                    bottomSheetState.show()
                                }
                            },
                        ) {
                            Icon(Icons.Outlined.Add, stringResource(R.string.action_newEntry_short))
                        }
                    } else {
                        ExtendedFloatingActionButton(
                            icon = { Icon(Icons.Outlined.Add, null) },
                            text = { Text(stringResource(R.string.action_newEntry_short)) },
                            containerColor = MaterialTheme.colorScheme.primary,
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                scope.launch {
                                    bottomSheetState.show()
                                }
                            },
                        )
                    }
                }
            },
            content = { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "entries"
                ) {
                    composable("entries") {
                        CounterEntries(counter, increments, countersListViewModel, innerPadding)
                    }
                    composable("settings") {
                        CounterSettings(
                            counter,
                            countersListViewModel,
                            innerPadding
                        )
                    }
                }
            },
            bottomBar = {
                Column(Modifier.zIndex(1F)) {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.Outlined.List, contentDescription = null) },
                            label = { Text(stringResource(R.string.text_entries_short)) },
                            selected = currentDestination?.hierarchy?.any { it.route == "entries" } == true,
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                navController.navigate("entries") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                            label = { Text(stringResource(R.string.text_counterSettings_short)) },
                            selected = currentDestination?.hierarchy?.any { it.route == "settings" } == true,
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                                navController.navigate("settings") {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                    Surface(
                        modifier = Modifier
                            .navigationBarsHeight()
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.surface,
                        shape = RectangleShape,
                        tonalElevation = 3.dp
                    ) {}
                }
            }
        )
    }
}