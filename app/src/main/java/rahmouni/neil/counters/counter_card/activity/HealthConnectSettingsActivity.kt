package rahmouni.neil.counters.counter_card.activity

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
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.PriorityHigh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
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
                    title = "Sync with Health Connect",
                    checked = (counter?.healthConnectEnabled ?: false)
                            && healthConnect.isAvailable(activity),
                    enabled = healthConnect.isAvailable(activity)
                ) { //TODO str
                    countersListViewModel.updateCounter(
                        counter!!.copy(
                            healthConnectEnabled = it
                        ).toCounter()
                    )
                }
            }
            when {
                healthConnect.hasSufficientSdk() -> item { //TODO finish
                    Banner(
                        title = "Start setup", //TODO str
                        description = "Start the setup to link Counters & Health Connect", //TODO str
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
                        title = "Incompatible Device", //TODO str
                        description = "Health Connect requires having at least Android Oreo (8.1) to work.", //TODO str
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
                            Text("Continue anyway")
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
                ActivityInfo( //TODO str
                    "Synchronize this counter with Health Connect compatible apps, such as Google Fit or Samsung Health.",
                    Modifier.padding(24.dp)
                )
            }
        }
    }
}