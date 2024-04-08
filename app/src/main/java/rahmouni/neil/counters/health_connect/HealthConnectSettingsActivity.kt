package rahmouni.neil.counters.health_connect

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.automirrored.outlined.Launch
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.PriorityHigh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CounterAugmented
import rahmouni.neil.counters.database.CountersListViewModel
import rahmouni.neil.counters.database.CountersListViewModelFactory
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.ActivityInfo
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.banner.Banner
import rahmouni.neil.counters.utils.header.HeaderSwitch
import rahmouni.neil.counters.utils.openChromeCustomTab
import rahmouni.neil.counters.utils.tiles.TileClick
import rahmouni.neil.counters.utils.tiles.TileDialogRadioButtons
import rahmouni.neil.counters.utils.tiles.TileDialogRadioList
import rahmouni.neil.counters.utils.tiles.TileHeader

class HealthConnectSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        Firebase.dynamicLinks.getDynamicLink(intent)

        val counterID: Int = intent.getIntExtra("counterID", 0)

        val countersListViewModel by viewModels<CountersListViewModel> {
            CountersListViewModelFactory((this.applicationContext as CountersApplication).countersListRepository)
        }

        setContent {
            CountersTheme {
                androidx.compose.material.Surface {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        tonalElevation = 1.dp,
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        HealthConnectSettingsPage(
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

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun HealthConnectSettingsPage(
    counterID: Int,
    countersListViewModel: CountersListViewModel,
    healthConnectManager: HealthConnectManager
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current
    val lifecycleState by LocalLifecycleOwner.current.lifecycle.observeAsState()
    val remoteConfig = FirebaseRemoteConfig.getInstance()
    val availability by healthConnectManager.availability

    val counter: CounterAugmented? by countersListViewModel.getCounter(counterID).observeAsState()
    val permissionsGranted = rememberSaveable { mutableStateOf(false) }

    var exerciseType by rememberSaveable {
        mutableStateOf(
            counter?.healthConnectExerciseType ?: HealthConnectExerciseType.BACK_EXTENSION
        )
    }
    var dataType by rememberSaveable {
        mutableStateOf(
            counter?.healthConnectDataType ?: HealthConnectDataType.REPETITIONS
        )
    }

    LaunchedEffect(lifecycleState) {
        permissionsGranted.value =
            availability == HealthConnectAvailability.INSTALLED && healthConnectManager.hasAllPermissions()
    }

    LaunchedEffect(Unit) {
        exerciseType =
            counter?.healthConnectExerciseType ?: HealthConnectExerciseType.BACK_EXTENSION
        dataType = counter?.healthConnectDataType ?: HealthConnectDataType.REPETITIONS
        permissionsGranted.value =
            availability == HealthConnectAvailability.INSTALLED && healthConnectManager.hasAllPermissions()
    }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.healthConnectSettingsActivity_topbar_title)) },
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
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.healthConnectSettingsActivity_topbar_icon_back_contentDescription)
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
                    title = stringResource(R.string.healthConnectSettingsActivity_header_title),
                    checked = permissionsGranted.value && (counter?.healthConnectEnabled
                        ?: false),
                    enabled = permissionsGranted.value
                ) {
                    countersListViewModel.updateCounter(
                        counter!!.copy(
                            healthConnectEnabled = it
                        ).toCounter()
                    )
                }
            }
            when {
                permissionsGranted.value -> item {
                    TileHeader(stringResource(R.string.healthConnectSettingsActivity_tile_general_headerTitle))
                    // ExerciseType
                    TileDialogRadioList(
                        title = stringResource(R.string.healthConnectSettingsActivity_tile_exerciseType_title),
                        icon = Icons.Outlined.FitnessCenter,
                        values = HealthConnectExerciseType.values().asList(),
                        selected = exerciseType,
                    ) {
                        exerciseType = it as HealthConnectExerciseType

                        var newCounter = counter!!.copy(
                            healthConnectExerciseType = it,
                        )

                        if (!exerciseType.dataTypes.contains(dataType)) {
                            dataType = exerciseType.defaultDataType
                            newCounter = counter!!.copy(
                                healthConnectDataType = exerciseType.defaultDataType,
                            )
                        }

                        countersListViewModel.updateCounter(
                            newCounter.toCounter()
                        )
                    }
                    // DataType
                    TileDialogRadioButtons(
                        title = stringResource(R.string.healthConnectSettingsActivity_tile_dataType_title),
                        icon = Icons.Outlined.Category,
                        values = exerciseType.dataTypes.toList(),
                        selected = dataType,
                    ) {
                        dataType = it as HealthConnectDataType
                        countersListViewModel.updateCounter(
                            counter!!.copy(
                                healthConnectDataType = it
                            ).toCounter()
                        )
                    }
                    /*TileDialogRadioList(
                        title = stringResource(R.string.healthConnectSettingsActivity_tile_activityType_title),
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
                    }*/
                    TileClick(
                        title = stringResource(R.string.healthConnectSettingsActivity_tile_openApp_title),
                        icon = Icons.AutoMirrored.Outlined.Launch
                    ) {
                        val launchIntent =
                            activity.packageManager.getLaunchIntentForPackage("com.google.android.apps.healthdata")
                        if (launchIntent != null) {
                            activity.startActivity(launchIntent)
                        }
                    }
                    HorizontalDivider(Modifier.padding(top = 8.dp))
                }

                availability != HealthConnectAvailability.NOT_SUPPORTED -> item {
                    Banner(
                        title = stringResource(R.string.healthConnectSettingsActivity_banner_setup_title),
                        description = stringResource(R.string.healthConnectSettingsActivity_banner_setup_description),
                        icon = Icons.Outlined.Link,
                        Modifier.padding(8.dp)
                    ) {
                        Button({
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            activity.startActivity(
                                Intent(activity, HealthConnectSetupActivity::class.java)
                            )
                        }) {
                            Text(stringResource(R.string.healthConnectSettingsActivity_banner_setup_button_text))
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Icon(Icons.AutoMirrored.Outlined.ArrowForward, null)
                        }
                    }
                }

                else -> item {
                    Banner(
                        title = stringResource(R.string.healthConnectSettingsActivity_banner_incompatibleDevice_title),
                        description = stringResource(R.string.healthConnectSettingsActivity_banner_incompatibleDevice_description),
                        icon = Icons.Outlined.PriorityHigh,
                        Modifier.padding(8.dp),
                        containerColor = MaterialTheme.colorScheme.errorContainer,
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
                            Text(stringResource(R.string.healthConnectSettingsActivity_banner_incompatibleDevice_textButton_text))
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
                            Text(stringResource(R.string.healthConnectSettingsActivity_banner_incompatibleDevice_button_text))
                        }
                    }
                }
            }

            item {
                ActivityInfo(
                    stringResource(R.string.healthConnectSettingsActivity_activityInfo_description),
                    Modifier.padding(24.dp)
                )
            }
        }
    }
}