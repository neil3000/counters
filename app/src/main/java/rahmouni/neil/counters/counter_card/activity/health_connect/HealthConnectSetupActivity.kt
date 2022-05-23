package rahmouni.neil.counters.counter_card.activity.health_connect

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.health.connect.client.permission.HealthDataRequestPermissions
import androidx.health.connect.client.permission.Permission
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.R
import rahmouni.neil.counters.healthConnect
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.openPlayStoreUrl
import rahmouni.neil.counters.utils.tiles.TileStep

class HealthConnectSetupActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val hcActResult = registerForActivityResult(HealthDataRequestPermissions()) {}

        setContent {
            CountersTheme {
                ProvideWindowInsets {
                    androidx.compose.material.Surface {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            tonalElevation = 1.dp,
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            HealthConnectSetupPage(hcActResult)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun HealthConnectSetupPage(
    hcActResult: ActivityResultLauncher<Set<Permission>>,
) {
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.observeAsState()

    var clientAvailable by rememberSaveable { mutableStateOf(healthConnect.isClientAvailable()) }
    var hasPermissions by rememberSaveable { mutableStateOf(healthConnect.hasPermissions()) }

    LaunchedEffect(key1 = lifecycleState) {
        healthConnect.initialize(context)
        clientAvailable = healthConnect.isClientAvailable()
        hasPermissions = healthConnect.hasPermissions()
    }

    Scaffold(
        modifier = Modifier.statusBarsPadding(),
        topBar = {
            SmallTopAppBar(
                title = { },
                actions = {
                    SettingsDots(screenName = "HealthConnectSetupActivity") {}
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
                }
            )
        },
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 400.dp),
            Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            item {
                Column(
                    Modifier.padding(horizontal = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        Modifier
                            .padding(16.dp)
                            .size(96.dp),
                        shape = CircleShape,
                        tonalElevation = -LocalAbsoluteTonalElevation.current
                    ) {
                        Icon(
                            Icons.Outlined.Link, "Link Health Connect",
                            Modifier
                                .fillMaxSize()
                                .padding(24.dp)
                        ) //TODO str
                    }
                    Text(
                        "Link Health Connect",
                        style = MaterialTheme.typography.headlineSmall
                    ) //TODO str
                }
            }
            item {
                Column {
                    MenuDefaults.Divider()
                    TileStep(
                        title = stringResource(R.string.text_stepX, 1),
                        description = stringResource(R.string.action_installHealthConnectApp),
                        done = clientAvailable
                    ) {
                        openPlayStoreUrl(
                            activity,
                            remoteConfig.getString("healthConnect_playStore_url")
                        )
                    }
                    MenuDefaults.Divider()
                    TileStep(
                        title = stringResource(R.string.text_stepX, 2),
                        description = stringResource(R.string.action_acceptPermissions),
                        done = hasPermissions,
                        enabled = clientAvailable
                    ) {
                        hcActResult.launch(healthConnect.permissions)
                    }
                    MenuDefaults.Divider()

                    Button(onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                        activity.finish()
                    },
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        enabled = clientAvailable && hasPermissions
                    ) {
                        Text(stringResource(R.string.action_continue))
                    }
                }
            }
        }
    }
}

@Composable
fun Lifecycle.observeAsState(): State<Lifecycle.Event> {
    val state = remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(this) {
        val observer = LifecycleEventObserver { _, event ->
            state.value = event
        }
        this@observeAsState.addObserver(observer)
        onDispose {
            this@observeAsState.removeObserver(observer)
        }
    }
    return state
}