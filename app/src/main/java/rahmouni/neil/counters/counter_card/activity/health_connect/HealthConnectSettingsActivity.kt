package rahmouni.neil.counters.counter_card.activity.health_connect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
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
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.healthConnect
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.ActivityInfo
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.banner.Banner
import rahmouni.neil.counters.utils.header.HeaderSwitch
import rahmouni.neil.counters.utils.openChromeCustomTab
import rahmouni.neil.counters.utils.tiles.TileClick
import rahmouni.neil.counters.utils.tiles.TileDialogRadioList
import rahmouni.neil.counters.utils.tiles.TileHeader

class HealthConnectSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val counterID: Int = intent.getIntExtra("counterID", 0)

        val countersListViewModel by viewModels<CountersListViewModel> {
            CountersListViewModelFactory((this.applicationContext as CountersApplication).countersListRepository)
        }

        setContent {
            CountersTheme {
                ProvideWindowInsets {
                    androidx.compose.material.Surface {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            tonalElevation = 1.dp,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            HealthConnectSettingsPage(counterID, countersListViewModel)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun HealthConnectSettingsPage(
    counterID: Int,
    countersListViewModel: CountersListViewModel,
) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    val counter: CounterAugmented? by countersListViewModel.getCounter(counterID).observeAsState()

    var activityType by rememberSaveable {
        mutableStateOf(
            counter?.healthConnectType ?: HealthConnectType.SQUAT
        )
    }

    LaunchedEffect(counter) {
        activityType = counter?.healthConnectType ?: HealthConnectType.SQUAT
    }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .statusBarsPadding(),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.text_healthConnect)) },
                actions = {
                    SettingsDots(screenName = "HealthConnectSettingsActivity") {}
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            activity.finish()
                        }
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
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxHeight()) {
            item {
                Surface(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = -LocalAbsoluteTonalElevation.current,
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_health_connect_full),
                            null,
                            Modifier.fillMaxWidth(),
                            Color.Unspecified
                        )
                    }
                }
            }
            item {
                HeaderSwitch(
                    title = stringResource(R.string.text_synchronization),
                    checked = (counter?.healthConnectEnabled ?: false)
                            && healthConnect.isAvailable(),
                    enabled = healthConnect.isAvailable()
                ) {
                    countersListViewModel.updateCounter(
                        counter!!.copy(
                            healthConnectEnabled = it
                        ).toCounter()
                    )
                }
            }
            when {
                healthConnect.isAvailable() -> item {
                    TileHeader(stringResource(R.string.header_general))
                    TileDialogRadioList(
                        title = stringResource(R.string.text_activityType),
                        icon = Icons.Outlined.FitnessCenter,
                        values = HealthConnectType.values().asList(),
                        selected = activityType,
                    ) {
                        activityType = it as HealthConnectType
                        countersListViewModel.updateCounter(
                            counter!!.copy(
                                healthConnectType = it
                            ).toCounter()
                        )
                    }
                    TileClick(
                        title = stringResource(R.string.action_openHealthConnect),
                        icon = Icons.Outlined.Launch
                    ) {
                        val launchIntent =
                            activity.packageManager.getLaunchIntentForPackage("com.google.android.apps.healthdata")
                        if (launchIntent != null) {
                            activity.startActivity(launchIntent)
                        }
                    }
                    MenuDefaults.Divider()
                }
                healthConnect.hasSufficientSdk() -> item {
                    Banner(
                        title = stringResource(R.string.action_startSetup),
                        description = stringResource(R.string.action_startSetupToLinkCountersAndHealthConnect),
                        icon = Icons.Outlined.Link
                    ) {
                        Button({
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            activity.startActivity(
                                Intent(activity, HealthConnectSetupActivity::class.java)
                            )
                        }) {
                            Text(stringResource(R.string.action_letsGo))
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Icon(Icons.Outlined.ArrowForward, null)
                        }
                    }
                }
                else -> item {
                    Banner(
                        title = stringResource(R.string.text_incompatibleDevice),
                        description = stringResource(R.string.text_healthConnectRequiresHavingAtLeastAndroidOreoToWork),
                        icon = Icons.Outlined.PriorityHigh,
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ) {
                        TextButton(
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.onErrorContainer),
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                openChromeCustomTab(
                                    activity,
                                    remoteConfig.getString("healthConnect_androidMinSdkWiki_url")
                                )
                            },
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(stringResource(R.string.action_continueAnyway))
                        }
                        Button(
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                            onClick = {
                                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                                openChromeCustomTab(
                                    activity,
                                    remoteConfig.getString("healthConnect_androidMinSdk_url")
                                )
                            }) {
                            Text(stringResource(R.string.action_learnMore))
                        }
                    }
                }
            }

            item {
                ActivityInfo(
                    stringResource(R.string.action_synchronizeThisCounterWithHealthConnectCompatibleApps),
                    Modifier.padding(24.dp)
                )
            }
        }
    }
}