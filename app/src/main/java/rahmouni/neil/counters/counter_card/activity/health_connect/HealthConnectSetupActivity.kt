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
import androidx.health.connect.client.permission.Permission
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.insets.ProvideWindowInsets
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

        val requestPermissionActivityContract =
            healthConnect.client?.permissionController?.createRequestPermissionActivityContract()
        //val hcActResult = registerForActivityResult(HealthDataRequestPermissions()) {}
        var hcActResult: ActivityResultLauncher<Set<Permission>>? = null
        if (requestPermissionActivityContract != null && healthConnect.permissions != null) {
            hcActResult = registerForActivityResult(requestPermissionActivityContract) {}
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
    hcActResult: ActivityResultLauncher<Set<Permission>>?,
) {
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.observeAsState()

    var clientAvailable by rememberSaveable { mutableStateOf(healthConnect.isClientAvailable()) }
    var hasPermissions by rememberSaveable { mutableStateOf(healthConnect.hasPermissions()) }
    var permClicks by rememberSaveable { mutableStateOf(0) }

    LaunchedEffect(key1 = lifecycleState) {
        healthConnect.initialize(context)
        clientAvailable = healthConnect.isClientAvailable()
        hasPermissions = healthConnect.hasPermissions()
    }

    Scaffold(
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
                            contentDescription = stringResource(R.string.healthConnectSetupActivity_topbar_icon_back_contentDescription)
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
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = CircleShape,
                    ) {
                        Icon(
                            Icons.Outlined.Link,
                            null,
                            Modifier
                                .fillMaxSize()
                                .padding(24.dp),
                            MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                    Text(
                        stringResource(R.string.healthConnectSetupActivity_text_linkHealthConnect_text),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
            item {
                Column {
                    Divider()
                    TileStep(
                        title = stringResource(
                            R.string.healthConnectSetupActivity_tile_step_title,
                            1
                        ),
                        description = stringResource(R.string.healthConnectSetupActivity_tile_install_secondary),
                        done = clientAvailable
                    ) {
                        openPlayStoreUrl(
                            activity,
                            remoteConfig.getString("healthConnect_playStore_url")
                        )
                    }
                    Divider()
                    TileStep(
                        title = stringResource(
                            R.string.healthConnectSetupActivity_tile_step_title,
                            2
                        ),
                        description = stringResource(R.string.healthConnectSetupActivity_tile_permissions_secondary),
                        done = hasPermissions,
                        enabled = clientAvailable
                    ) {
                        if (permClicks < 2) {
                            hcActResult?.launch(healthConnect.permissions)
                            permClicks += 1
                        }else{
                            val launchIntent =
                                activity.packageManager.getLaunchIntentForPackage("com.google.android.apps.healthdata")
                            if (launchIntent != null) {
                                activity.startActivity(launchIntent)
                            }
                        }
                    }
                    Divider()

                    Button(
                        onClick = {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            activity.finish()
                        },
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        enabled = clientAvailable && hasPermissions
                    ) {
                        Text(stringResource(R.string.healthConnectSetupActivity_button_continue))
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