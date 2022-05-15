package rahmouni.neil.counters.settings

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.R
import rahmouni.neil.counters.prefs
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.sendEmail
import rahmouni.neil.counters.utils.tiles.*

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setContent {
            CountersTheme {
                ProvideWindowInsets {
                    androidx.compose.material.Surface {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            SettingsPage()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun SettingsPage() {
        val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current

    val remoteConfig = FirebaseRemoteConfig.getInstance()

    val showDebug = android.provider.Settings.Secure.getInt(
        activity.contentResolver,
        android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
    ) != 0

    var startWeekDay: StartWeekDay? by rememberSaveable { mutableStateOf(prefs.startWeekDay) }
    var weekDisplay: WeekDisplay? by rememberSaveable { mutableStateOf(prefs.weekDisplay) }
    var debugMode: Boolean? by rememberSaveable { mutableStateOf(prefs.debugMode) }

    Scaffold(
        modifier = Modifier
            .statusBarsPadding()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.text_settings)) },
                actions = {
                    SettingsDots(screenName = "SettingsActivity") {}
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
        LazyColumn(contentPadding = innerPadding) {
            item { TileHeader(stringResource(R.string.header_general)) }
            item {
                TileDialogRadioButtons(
                    title = stringResource(R.string.text_firstDayOfTheWeek),
                    icon = Icons.Outlined.CalendarViewMonth,
                    values = StartWeekDay.values().toList(),
                    selected = startWeekDay ?: StartWeekDay.LOCALE,
                ) {
                    startWeekDay = it as StartWeekDay
                    prefs.startWeekDay = it
                }
            }
            item {
                TileDialogRadioButtons(
                    title = stringResource(R.string.action_displayWeekAs),
                    icon = Icons.Outlined.Tag,
                    values = WeekDisplay.values().toList(),
                    selected = weekDisplay ?: WeekDisplay.NUMBER,
                ) {
                    weekDisplay = it as WeekDisplay
                    prefs.weekDisplay = it
                }
            }
            item {
                TileStartActivity(
                    title = stringResource(R.string.text_dataAndPrivacy),
                    icon = Icons.Outlined.Shield,
                    activity = DataSettingsActivity::class.java
                )
            }
            item { MenuDefaults.Divider() }

            item { TileHeader(stringResource(R.string.header_about)) }
            item {
                TileOpenPlayStoreUrl(
                    title = stringResource(R.string.action_seeOnThePlayStore),
                    icon = Icons.Outlined.StarOutline,
                    url = remoteConfig.getString("play_store_url")
                )
            }
            item {
                TileOpenCustomTab(
                    title = stringResource(R.string.text_changelog),
                    icon = Icons.Outlined.NewReleases,
                    url = remoteConfig.getString("changelog_url")
                )
            }
            item {
                TileClick(
                    title = stringResource(R.string.text_helpTranslateTheApp),
                    icon = Icons.Outlined.Translate
                ){
                    sendEmail(activity, remoteConfig.getString("feedback_email"), "Want to help translate")
                }
            }
            

            if (showDebug) {
                item { MenuDefaults.Divider() }

                item { TileHeader("Debug") }
                item {
                    TileClick(
                        title = "\n" +
                                "For experimental users only\n" +
                                "Shown because you have dev settings turned on\n" +
                                "\n" +
                                "SOME CHANGES APPLY ON RESTART" +
                                "\n",
                        icon = null
                    ) {}
                }
                item {
                    TileSwitch(
                        title = "DEBUG_MODE",
                        icon = Icons.Outlined.Code,
                        checked = debugMode ?: false
                    ) {
                        prefs.debugMode = it
                        debugMode = it
                    }
                }
                item {
                    TileRemoteConfig()
                }
            }
            item {
                Box(Modifier.navigationBarsPadding())
            }
        }
    }
}