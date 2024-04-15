package rahmouni.neil.counters.counterActivity

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.PathParser.createPathFromPathData
import androidx.core.view.WindowCompat
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.R
import rahmouni.neil.counters.counterActivity.goal.GoalBar
import rahmouni.neil.counters.counterActivity.goal.GoalKonfetti
import rahmouni.neil.counters.counterActivity.statCount.StatCountProvider
import rahmouni.neil.counters.counter_card.new_increment.NewIncrement
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.database.Increment
import rahmouni.neil.counters.health_connect.HealthConnectManager
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
                androidx.compose.material.Surface {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CounterPage(
                            counterID,
                            countersListViewModel,
                            (application as CountersApplication).healthConnectManager
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun CounterPage(
    counterID: Int,
    countersListViewModel: CountersListViewModel,
    healthConnectManager: HealthConnectManager
) {
    val activity = (LocalContext.current as Activity)
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()
    //val windowSize = calculateWindowSizeClass(activity = activity)
    val context = LocalContext.current

    val bottomSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )

    val counter: CounterAugmented? by countersListViewModel.getCounter(counterID).observeAsState()
    val increments: List<Increment>? by countersListViewModel.getCounterIncrements(counterID)
        .observeAsState()

    //val bottomBarVisible =
    //    windowSize.widthSizeClass == WindowWidthSizeClass.Compact && windowSize.heightSizeClass != WindowHeightSizeClass.Compact

    var konpos by remember { mutableStateOf(Offset.Zero) }

    RoundedBottomSheet(
        bottomSheetState,
        {
            if (counter != null) {
                NewIncrement(counter, countersListViewModel, healthConnectManager) {
                    scope.launch {
                        bottomSheetState.hide()
                    }
                }
            }
        }
    ) {
        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(counter?.getDisplayName(context) ?: "Counter") },
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
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = stringResource(R.string.counterActivity_topbar_icon_back_contentDescription)
                            )
                        }
                    }
                )
            },
            content = { innerPadding ->
                Row(Modifier.padding(innerPadding)) {
                    if (counter != null && increments != null) {
                        Box {
                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 400.dp),
                                Modifier.fillMaxSize()
                            ) {
                                item {
                                    Column(
                                        Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        MainCount(
                                            count = counter!!.getCount(),
                                            valueType = counter!!.valueType
                                        )

                                        StatCountProvider(counter!!, countersListViewModel)

                                        Spacer(Modifier.height(8.dp))

                                        SuggestionsCard(
                                            counter!!,
                                            countersListViewModel,
                                            bottomSheetState
                                        )
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
                                        verticalArrangement = spacedBy(16.dp)
                                    ) {
                                        if (counter?.isGoalEnabled() == true) {
                                            GoalBar(
                                                counter!!.getGoalProgress(),
                                                counter!!,
                                                Modifier.onGloballyPositioned {
                                                    konpos =
                                                        (it.parentLayoutCoordinates?.positionInParent()
                                                            ?: Offset.Zero).plus(it.boundsInParent().center)
                                                }
                                            )
                                        }

                                        LatestEntries(
                                            increments!!,
                                            countersListViewModel,
                                            counter!!,
                                            (if (big) 8 else 5) - (if (counter!!.isGoalEnabled())
                                                2 else 0)
                                        )
                                    }
                                }
                            }
                            GoalKonfetti(counter!!, konpos)
                        }
                    }
                }
            },
            bottomBar = {
                BottomAppBar(
                    actions = {
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
                                Icons.AutoMirrored.Outlined.List,
                                stringResource(R.string.counterActivity_bottomBar_icon_entries_contentDescription)
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
                                stringResource(R.string.counterActivity_bottomBar_icon_settings_contentDescription)
                            )
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
        )
    }
}

class Shape1 : androidx.compose.ui.graphics.Shape {
    @SuppressLint("RestrictedApi")
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
        val matcher = Pattern.compile("\\d+[.]?(\\d+)?")
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