package rahmouni.neil.counters.counterActivity

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.view.WindowCompat
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.launch
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.R
import rahmouni.neil.counters.counter_card.activity.CounterEntries
import rahmouni.neil.counters.counter_card.new_increment.NewIncrement
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.database.Increment
import rahmouni.neil.counters.health_connect.HealthConnectManager
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.RoundedBottomSheetOld
import rahmouni.neil.counters.utils.SettingsDots

class CounterEntriesActivity : ComponentActivity() {
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
                        CounterEntriesPage(
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

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CounterEntriesPage(
    counterID: Int,
    countersListViewModel: CountersListViewModel,
    healthConnectManager: HealthConnectManager
) {
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val haptic = LocalHapticFeedback.current
    val activity = (LocalContext.current as Activity)
    val remoteConfig = FirebaseRemoteConfig.getInstance()


    val counter: CounterAugmented? by countersListViewModel.getCounter(counterID).observeAsState()
    val increments: List<Increment>? by countersListViewModel.getCounterIncrements(counterID)
        .observeAsState()

    if (remoteconfig.getBoolean("i_233")) { // New BottomSheet
        val sheetState = androidx.compose.material3.rememberModalBottomSheetState()
        var showBottomSheet: Boolean by rememberSaveable { mutableStateOf(false) }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                NewIncrement(counter, countersListViewModel, healthConnectManager) {
                    scope.launch { sheetState.hide() }
                        .invokeOnCompletion { showBottomSheet = false }
                }
            }
        }

        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                LargeTopAppBar(
                    title = {
                        Text(
                            stringResource(
                                R.string.counterEntriesActivity_topbar_title,
                                counter?.getDisplayName(activity) ?: "Counter"
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        SettingsDots(screenName = "CounterEntriesActivity") {}
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                activity.finish()
                            }
                        ) {
                            Icon(
                                Icons.AutoMirrored.Outlined.ArrowBack,
                                contentDescription = stringResource(R.string.counterEntriesActivity_topbar_icon_back_contentDescription)
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(onClick = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                    showBottomSheet = true
                }) {
                    Icon(Icons.Outlined.Add, null)
                    Text(stringResource(id = R.string.counterActivity_fab_newEntry_contentDescription))
                }
            }
        ) { innerPadding ->

            Column {
                CounterEntries(
                    counter = counter,
                    increments = increments,
                    countersListViewModel = countersListViewModel,
                    contentPadding = innerPadding
                )
            }
        }
    } else {
        val bottomSheetState = rememberModalBottomSheetState(
            initialValue = ModalBottomSheetValue.Hidden,
        )

        RoundedBottomSheetOld(
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
                modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                topBar = {
                    LargeTopAppBar(
                        title = {
                            Text(
                                stringResource(
                                    R.string.counterEntriesActivity_topbar_title,
                                    counter?.getDisplayName(activity) ?: "Counter"
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        actions = {
                            SettingsDots(screenName = "CounterEntriesActivity") {}
                        },
                        navigationIcon = {
                            IconButton(
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                                    activity.finish()
                                }
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Outlined.ArrowBack,
                                    contentDescription = stringResource(R.string.counterEntriesActivity_topbar_icon_back_contentDescription)
                                )
                            }
                        },
                        scrollBehavior = scrollBehavior
                    )
                },
                floatingActionButton = {
                    ExtendedFloatingActionButton(onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)

                        scope.launch {
                            bottomSheetState.show()
                        }
                    }) {
                        Icon(Icons.Outlined.Add, null)
                        Text(stringResource(id = R.string.counterActivity_fab_newEntry_contentDescription))
                    }
                }
            ) { innerPadding ->

                Column {
                    CounterEntries(
                        counter = counter,
                        increments = increments,
                        countersListViewModel = countersListViewModel,
                        contentPadding = innerPadding
                    )
                }
            }
        }
    }
}