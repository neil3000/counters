package rahmouni.neil.counters

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.launch
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.counter_card.CounterCard
import rahmouni.neil.counters.counter_card.NewIncrement
import rahmouni.neil.counters.counter_card.NewIncrementExperiment
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.settings.SettingsActivity
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.ContributeTranslateBanner
import rahmouni.neil.counters.utils.FullscreenDynamicSVG
import rahmouni.neil.counters.utils.RoundedBottomSheet
import rahmouni.neil.counters.utils.SettingsDots

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

        val remoteConfig = FirebaseRemoteConfig.getInstance()
        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        remoteConfig.fetchAndActivate()

        FirebaseCrashlytics.getInstance()
            .setCrashlyticsCollectionEnabled(prefs.crashlyticsEnabled && !BuildConfig.DEBUG)

        analytics?.setAnalyticsCollectionEnabled(prefs.analyticsEnabled && !BuildConfig.DEBUG)
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
    val remoteConfig = FirebaseRemoteConfig.getInstance()

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

    RoundedBottomSheet(bottomSheetNewIncrementState, (if (remoteConfig.getBoolean("issue84__new_increment_redesign")) 1.dp else 0.dp), {
        if (remoteConfig.getBoolean("issue84__new_increment_redesign")) {
            NewIncrementExperiment(
                counter = if (bottomSheetNewIncrementCounterID == null || countersList.isEmpty()) null else countersList.find { it.uid == bottomSheetNewIncrementCounterID },
                countersListViewModel = countersListViewModel
            ) {
                scope.launch {
                    bottomSheetNewIncrementState.hide()
                    bottomSheetNewIncrementCounterID = null
                }

            }
        } else {
            NewIncrement(
                counter = if (bottomSheetNewIncrementCounterID == null || countersList.isEmpty()) null else countersList.find { it.uid == bottomSheetNewIncrementCounterID },
                countersListViewModel = countersListViewModel
            ) {
                scope.launch {
                    bottomSheetNewIncrementState.hide()
                    bottomSheetNewIncrementCounterID = null
                }

            }
        }
    }) {
        RoundedBottomSheet(bottomSheetNewCounterState, (if (remoteConfig.getBoolean("issue85__new_counter_redesign")) 1.dp else 0.dp), {
            if (remoteConfig.getBoolean("issue85__new_counter_redesign")) {
                NewCounterExperiment(countersListViewModel) {
                    scope.launch {
                        bottomSheetNewCounterState.hide()
                    }
                }
            }else {
                NewCounter(countersListViewModel) {
                    scope.launch {
                        bottomSheetNewCounterState.hide()
                    }
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
                            SettingsDots(screenName = "MainActivity") {
                                DropdownMenuItem(
                                    text = { Text(stringResource(R.string.text_settings)) },
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
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.navigationBarsPadding()
                    )
                }
            ) {
                if (countersList.isNotEmpty()) {
                    Column(Modifier.padding(it)) {
                        ContributeTranslateBanner()
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 180.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(8.dp)
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
                    }
                } else {
                    FullscreenDynamicSVG(R.drawable.ic_balloons, R.string.text_noCountersYet)
                }
            }
        }
    }
}