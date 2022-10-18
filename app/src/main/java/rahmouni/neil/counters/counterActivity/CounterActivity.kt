package rahmouni.neil.counters.counterActivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.PathParser.createPathFromPathData
import androidx.core.view.WindowCompat
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.launch
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.R
import rahmouni.neil.counters.counterActivity.statCount.StatCountProvider
import rahmouni.neil.counters.counter_card.activity.CounterEntries
import rahmouni.neil.counters.counter_card.activity.CounterSettings
import rahmouni.neil.counters.counter_card.activity.graph.CounterGraph
import rahmouni.neil.counters.counter_card.new_increment.NewIncrement
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.database.Increment
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.RoundedBottomSheet
import rahmouni.neil.counters.utils.SettingsDots
import java.util.regex.Pattern

class CounterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val counterID: Int = intent.getIntExtra("counterID", 0)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        Firebase.dynamicLinks.getDynamicLink(intent)

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
    ExperimentalMaterial3WindowSizeClassApi::class
)
@Composable
fun CounterPage(counterID: Int, countersListViewModel: CountersListViewModel) {
    val activity = (LocalContext.current as Activity)
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    val rc = FirebaseRemoteConfig.getInstance()
    val navController = rememberNavController()
    val windowSize = calculateWindowSizeClass(activity = activity)
    val context = LocalContext.current

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val counter: CounterAugmented? by countersListViewModel.getCounter(counterID).observeAsState()
    val increments: List<Increment>? by countersListViewModel.getCounterIncrements(counterID)
        .observeAsState()

    val bottomBarVisible =
        windowSize.widthSizeClass == WindowWidthSizeClass.Compact && windowSize.heightSizeClass != WindowHeightSizeClass.Compact

    val navItemsLabels =
        if ((counter?.valueType?.hasStats != false) && rc.getBoolean("issue20__graph"))
            listOf(
                R.string.counterActivity_nav_entries_label,
                R.string.counterActivity_nav_stats_label,
                R.string.counterActivity_nav_settings_label
            )
        else listOf(
            R.string.counterActivity_nav_entries_label,
            R.string.counterActivity_nav_settings_label
        )
    val navItemsRoutes =
        if ((counter?.valueType?.hasStats != false) && rc.getBoolean("issue20__graph"))
            listOf("entries", "graph", "settings")
        else listOf("entries", "settings")
    val navItemsIcons =
        if ((counter?.valueType?.hasStats != false) && rc.getBoolean("issue20__graph"))
            listOf(Icons.Outlined.List, Icons.Outlined.ShowChart, Icons.Outlined.Settings)
        else listOf(Icons.Outlined.List, Icons.Outlined.Settings)

    fun moveToRoute(route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    RoundedBottomSheet(
        bottomSheetState,
        {
            if (counter != null) {
                NewIncrement(counter, countersListViewModel) {
                    scope.launch {
                        bottomSheetState.hide()
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                if (rc.getBoolean("issue190__revamp_counter_screen2")) {
                    CenterAlignedTopAppBar(
                        title = { Text(counter?.displayName ?: "Counter") },
                        actions = { SettingsDots(screenName = "CounterActivity") {} },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                    activity.finish()
                                },
                                modifier = Modifier.testTag("BACK_ARROW")
                            ) {
                                Icon(
                                    Icons.Outlined.ArrowBack,
                                    contentDescription = stringResource(R.string.counterActivity_topbar_icon_back_contentDescription)
                                )
                            }
                        }
                    )
                } else {
                    TopAppBar(
                        title = { Text(counter?.displayName ?: "Counter") },
                        actions = { SettingsDots(screenName = "CounterActivity") {} },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                    activity.finish()
                                },
                                modifier = Modifier.testTag("BACK_ARROW")
                            ) {
                                Icon(
                                    Icons.Outlined.ArrowBack,
                                    contentDescription = stringResource(R.string.counterActivity_topbar_icon_back_contentDescription)
                                )
                            }
                        }
                    )
                }
            },
            content = { innerPadding ->
                Row(Modifier.padding(innerPadding)) {
                    if (!bottomBarVisible) {
                        NavigationRail(Modifier.padding(horizontal = 8.dp), header = {
                            FloatingActionButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                    scope.launch {
                                        bottomSheetState.show()
                                    }
                                },
                            ) {
                                Icon(
                                    Icons.Outlined.Add,
                                    stringResource(R.string.counterActivity_fab_newEntry_contentDescription)
                                )
                            }
                        }) {
                            navItemsLabels.forEachIndexed { index, item ->
                                NavigationRailItem(
                                    icon = {
                                        Icon(
                                            navItemsIcons[index],
                                            contentDescription = stringResource(item)
                                        )
                                    },
                                    label = { Text(stringResource(item)) },
                                    selected = currentDestination?.hierarchy?.any { it.route == navItemsRoutes[index] } == true,
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                        moveToRoute(navItemsRoutes[index])
                                    }
                                )
                            }
                        }
                    }

                    if (rc.getBoolean("issue190__revamp_counter_screen2")) {
                        if (counter != null && increments != null) {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 400.dp),
                                Modifier.fillMaxSize(),
                                verticalArrangement = spacedBy(24.dp)
                            ) {
                                item {
                                    Column(
                                        Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = spacedBy(24.dp)
                                    ) {
                                        MainCount(
                                            count = counter!!.count + counter!!.resetValue,
                                            valueType = counter!!.valueType
                                        )

                                        StatCountProvider(counter!!, countersListViewModel)

                                        // SuggestionChips (WIP)
                                        /*
                                        Row(horizontalArrangement = spacedBy(8.dp)) {
                                            AssistChip(
                                                onClick = {  },
                                                label = { Text("Edit name") },
                                                leadingIcon = {
                                                    Icon(
                                                        Icons.Outlined.AutoAwesome,
                                                        null,
                                                        Modifier.scale(.85f)
                                                    )
                                                }
                                            )
                                        }*/
                                    }
                                }
                                var big = false
                                item(span = {
                                    big = maxLineSpan > 1
                                    GridItemSpan(1)
                                }) {
                                    Column(
                                        Modifier
                                            .padding(16.dp)
                                            .fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = spacedBy(24.dp)
                                    ) {
                                        LatestEntries(
                                            increments!!,
                                            countersListViewModel,
                                            counter!!,
                                            big
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        NavHost(
                            navController = navController,
                            startDestination = "entries"
                        ) {
                            composable("entries") {
                                CounterEntries(counter, increments, countersListViewModel, false)
                            }
                            composable("graph") {
                                CounterGraph(counter, increments, countersListViewModel)
                            }
                            composable("settings") {
                                CounterSettings(counter, countersListViewModel)
                            }
                        }
                    }
                }
            },
            bottomBar = {
                if (bottomBarVisible) {
                    BottomAppBar(
                        actions = {
                            if (rc.getBoolean("issue190__revamp_counter_screen2")) {
                                IconButton(onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                    if (counter != null) {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                CounterEntriesActivity::class.java
                                            ).putExtra(
                                                "counterID",
                                                counter!!.uid
                                            )
                                        )
                                    }
                                }) {
                                    Icon(
                                        Icons.Outlined.List,
                                        stringResource(R.string.counterActivity_nav_entries_label)
                                    )
                                }
                                IconButton(onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                    if (counter != null) {
                                        context.startActivity(
                                            Intent(
                                                context,
                                                CounterSettingsActivity::class.java
                                            ).putExtra(
                                                "counterID",
                                                counter!!.uid
                                            )
                                        )
                                    }
                                }) {
                                    Icon(
                                        Icons.Outlined.Settings,
                                        stringResource(R.string.counterActivity_nav_settings_label)
                                    )
                                }
                            } else {
                                navItemsLabels.forEachIndexed { index, item ->
                                    IconButton(onClick = {
                                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                        moveToRoute(navItemsRoutes[index])
                                    }) {
                                        Icon(
                                            navItemsIcons[index],
                                            contentDescription = stringResource(item)
                                        )
                                    }
                                }
                            }
                        },
                        floatingActionButton = {
                            FloatingActionButton(
                                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                    scope.launch {
                                        bottomSheetState.show()
                                    }
                                },
                            ) {
                                Icon(
                                    Icons.Outlined.Add,
                                    stringResource(R.string.counterActivity_fab_newEntry_contentDescription)
                                )
                            }
                        }
                    )
                }
            }
        )
    }
}

class Shape1 : androidx.compose.ui.graphics.Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val pathData =
            """M0.887,14.467C-2.845,5.875 5.875,-2.845 14.467,0.887l1.42,0.617a10.323,10.323 0,0 0,8.225 0l1.42,-0.617c8.593,-3.732 17.313,4.988 13.581,13.58l-0.617,1.42a10.323,10.323 0,0 0,0 8.225l0.617,1.42c3.732,8.593 -4.989,17.313 -13.58,13.581l-1.42,-0.617a10.323,10.323 0,0 0,-8.225 0l-1.42,0.617C5.874,42.845 -2.846,34.125 0.886,25.533l0.617,-1.42a10.323,10.323 0,0 0,0 -8.225l-0.617,-1.42Z"""
        val scaleX = size.width / 40
        val scaleY = size.height / 40
        return Outline.Generic(
            createPathFromPathData(
                resize(
                    pathData,
                    scaleX,
                    scaleY
                )
            ).asComposePath()
        )
    }

    private fun resize(pathData: String, scaleX: Float, scaleY: Float): String {
        val matcher = Pattern.compile("[0-9]+[.]?([0-9]+)?")
            .matcher(pathData) // match the numbers in the path
        val stringBuffer = StringBuffer()
        var count = 0
        while (matcher.find()) {
            val number = matcher.group().toFloat()
            matcher.appendReplacement(
                stringBuffer,
                (if (count % 2 == 0) number * scaleX else number * scaleY).toString()
            ) // replace numbers with scaled numbers
            ++count
        }
        return stringBuffer.toString() // return the scaled path
    }
}