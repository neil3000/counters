package rahmouni.neil.counters

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.launch
import rahmouni.neil.counters.counterActivity.goal.GoalKonfetti
import rahmouni.neil.counters.counter_card.CounterCard
import rahmouni.neil.counters.counter_card.new_increment.NewIncrement
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.health_connect.HealthConnectManager
import rahmouni.neil.counters.settings.SettingsActivity
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.FullscreenDynamicSVG
import rahmouni.neil.counters.utils.RoundedBottomSheet
import rahmouni.neil.counters.utils.SettingsDots

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        FirebaseDynamicLinks.getInstance()

        val countersListViewModel by viewModels<CountersListViewModel> {
            CountersListViewModelFactory((this.applicationContext as CountersApplication).countersListRepository)
        }

        setContent {
            CountersTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Home(
                        countersListViewModel,
                        (application as CountersApplication).healthConnectManager
                    )
                }
            }
        }
    }
}

@OptIn(
    ExperimentalMaterial3Api::class,
    androidx.compose.material.ExperimentalMaterialApi::class
)
@Composable
fun Home(countersListViewModel: CountersListViewModel, healthConnectManager: HealthConnectManager) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val localHapticFeedback = LocalHapticFeedback.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    val countersList: List<CounterAugmented> by countersListViewModel.allCounters.observeAsState(
        listOf()
    )
    val bottomSheetNewCounterState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    val bottomSheetNewIncrementState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
    )
    var bottomSheetNewIncrementCounterID: Int? by rememberSaveable { mutableStateOf(null) }

    RoundedBottomSheet(
        bottomSheetNewIncrementState,
        {
            NewIncrement(
                counter = if (bottomSheetNewIncrementCounterID == null || countersList.isEmpty()) null else countersList.find { it.uid == bottomSheetNewIncrementCounterID },
                countersListViewModel = countersListViewModel,
                healthConnectManager = healthConnectManager,
            ) {
                scope.launch {
                    bottomSheetNewIncrementState.hide()
                    bottomSheetNewIncrementCounterID = null
                }

            }
        }) {
        RoundedBottomSheet(
            bottomSheetNewCounterState,
            {
                NewCounter(countersListViewModel) {
                    scope.launch {
                        bottomSheetNewCounterState.hide()
                    }
                }
            }) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(stringResource(R.string.mainActivity_topbar_title))
                        },
                        navigationIcon = {
                            if (remoteConfig.getString("contributor_tag") != "null") {
                                IconButton(onClick = {
                                    localHapticFeedback.performHapticFeedback(
                                        HapticFeedbackType.LongPress
                                    )

                                    Toast.makeText(
                                        context,
                                        context.getString(
                                            R.string.mainActivity_topbar_icon_contributor_toast,
                                            remoteConfig.getString("contributor_tag")
                                        ),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }) {
                                    Icon(
                                        Icons.Outlined.VolunteerActivism,
                                        stringResource(R.string.mainActivity_topbar_icon_contributor_contentDescription)
                                    )
                                }
                            }
                        },
                        actions = {
                            SettingsDots(screenName = "MainActivity", divider = true) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.mainActivity_topbar_settings)) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Outlined.Settings,
                                            contentDescription = null
                                        )
                                    },
                                    onClick = {
                                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                        context.startActivity(
                                            Intent(context, SettingsActivity::class.java)
                                        )
                                    })
                            }
                        }
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        text = { Text(stringResource(R.string.mainActivity_fab_newCounter)) },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = null
                            )
                        },
                        onClick = {
                            scope.launch {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                bottomSheetNewCounterState.show()
                            }
                        }
                    )
                }
            ) {
                if (countersList.isNotEmpty()) {
                    val konposList: MutableMap<Int, Offset> =
                        remember { mutableMapOf() }
                    Box {
                        Column(Modifier.padding(it)) {
                            LongPressTipBanner()

                            LazyVerticalGrid(
                                columns = GridCells.Adaptive(minSize = 165.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(8.dp)
                            ) {
                                items(countersList) { counter ->
                                    CounterCard(
                                        counter,
                                        countersListViewModel,
                                        healthConnectManager,
                                        Modifier.onGloballyPositioned {
                                            konposList[counter.uid] =
                                                (it.parentLayoutCoordinates?.positionInRoot()
                                                    ?: Offset.Zero).copy()
                                                    .plus(it.boundsInParent().center)
                                        }
                                    ) {
                                        bottomSheetNewIncrementCounterID = counter.uid
                                        scope.launch {
                                            bottomSheetNewIncrementState.show()
                                        }
                                    }
                                }
                            }
                        }
                        countersList.forEach { counter ->
                            if (counter.isGoalEnabled()) {
                                GoalKonfetti(
                                    counter,
                                    konposList[counter.uid]?.copy() ?: Offset.Zero
                                )
                            }
                        }
                    }
                } else {
                    FullscreenDynamicSVG(
                        R.drawable.ic_balloons,
                        R.string.mainActivity_fdSVG_noCounters
                    )
                }
            }
        }
    }
}