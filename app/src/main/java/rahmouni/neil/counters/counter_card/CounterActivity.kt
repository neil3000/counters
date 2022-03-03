package rahmouni.neil.counters.counter_card

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
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
import rahmouni.neil.counters.database.CounterWithIncrements
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.options.DeleteOption
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.RoundedBottomSheet

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
                    // A surface container using the 'background' color from the theme
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

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun CounterPage(counterID: Int, countersListViewModel: CountersListViewModel) {
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val activity = (LocalContext.current as? Activity)
    val localHapticFeedback = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    val navController = rememberNavController()
    val offset = remember { Animatable(0F) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    LaunchedEffect(currentDestination) {
        offset.animateTo(if (currentDestination?.hierarchy?.any { it.route == "entries" } == true) 0F else 100F,
            tween(400))
    }

    val counterWithIncrements: CounterWithIncrements? by countersListViewModel.getCounterWithIncrements(
        counterID
    ).observeAsState()


    RoundedBottomSheet(bottomSheetState, false, {
        if (counterWithIncrements!=null) {
            NewIncrement(counterWithIncrements!!.counter, countersListViewModel) {
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
                    title = { Text(counterWithIncrements?.counter?.displayName ?: "Counter") },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                activity?.finish()
                            }
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                contentDescription = "Back"
                            ) //TODO i18n
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    icon = { Icon(Icons.Filled.Add, "New entry") }, //TODO i18n
                    text = { Text("New entry") },
                    modifier = Modifier.offset(y = offset.value.dp),
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        scope.launch {
                            bottomSheetState.show()
                        }
                    },
                )
            },
            content = { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = "entries",
                    Modifier.padding(innerPadding)
                ) {
                    composable("entries") {
                        if (counterWithIncrements!=null) {
                            LazyColumn {
                                if (counterWithIncrements!!.increments.isNotEmpty()) {
                                    items(counterWithIncrements!!.increments.reversed()) { increment ->
                                        IncrementEntry(increment, countersListViewModel)
                                        Divider()
                                    }
                                }
                            }
                        }
                    }
                    composable("settings") {
                        Column(Modifier.padding(innerPadding)) {
                            DeleteOption {
                                activity?.finish()
                                countersListViewModel.deleteCounterById(counterID)
                            }
                            Divider()
                        }
                    }
                }
            },
            bottomBar = {
                Column(Modifier.zIndex(1F)) {
                    NavigationBar {
                        NavigationBarItem(
                            icon = { Icon(Icons.Filled.List, contentDescription = null) },
                            label = { Text("Entries") },
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
                            icon = { Icon(Icons.Filled.Settings, contentDescription = null) },
                            label = { Text("Settings") },
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