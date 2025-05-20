package rahmouni.neil.counters.settings

import android.content.ClipData
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AccessibilityNew
import androidx.compose.material.icons.outlined.CalendarViewMonth
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.NewReleases
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.R
import rahmouni.neil.counters.prefs
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.feedback.FeedbackActivity
import rahmouni.neil.counters.utils.openChromeCustomTab
import rahmouni.neil.counters.utils.openPlayStoreUrl
import rahmouni.neil.counters.utils.tiles.TileClick
import rahmouni.neil.counters.utils.tiles.TileDialogRadioButtons
import rahmouni.neil.counters.utils.tiles.TileHeader
import rahmouni.neil.counters.utils.tiles.TileRemoteConfig
import rahmouni.neil.counters.utils.tiles.TileStartActivity
import rahmouni.neil.counters.utils.tiles.TileSwitch

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        setContent {
            CountersTheme {
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val activity = LocalActivity.current
    val localHapticFeedback = LocalHapticFeedback.current
    val clipboard = LocalClipboard.current

    val remoteConfig = FirebaseRemoteConfig.getInstance()

    val showDebug = android.provider.Settings.Secure.getInt(
        activity?.contentResolver,
        android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
    ) != 0

    var startWeekDay: StartWeekDay? by rememberSaveable { mutableStateOf(prefs.startWeekDay) }
    var weekDisplay: WeekDisplay? by rememberSaveable { mutableStateOf(prefs.weekDisplay) }
    var debugMode: Boolean? by rememberSaveable { mutableStateOf(prefs.debugMode) }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.settingsActivity_topbar_title)) },
                actions = {
                    SettingsDots(screenName = "SettingsActivity") {}
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            activity?.finish()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.settingsActivity_topbar_icon_back_contentDescription)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding) {

            // General
            item { TileHeader(stringResource(R.string.settingsActivity_tile_general_headerTitle)) }
            // FirstDayOfWeek
            item {
                TileDialogRadioButtons(
                    title = stringResource(R.string.settingsActivity_tile_firstDayOfWeek_title),
                    icon = Icons.Outlined.CalendarViewMonth,
                    values = StartWeekDay.entries,
                    selected = startWeekDay ?: StartWeekDay.LOCALE,
                ) {
                    startWeekDay = it as StartWeekDay
                    prefs.startWeekDay = it
                }
            }
            // DisplayWeekAs
            item {
                TileDialogRadioButtons(
                    title = stringResource(R.string.settingsActivity_tile_displayWeekAs_title),
                    icon = Icons.Outlined.Tag,
                    values = WeekDisplay.entries,
                    selected = weekDisplay ?: WeekDisplay.NUMBER,
                ) {
                    weekDisplay = it as WeekDisplay
                    prefs.weekDisplay = it
                }
            }
            // DataAndPrivacy
            item {
                TileStartActivity(
                    title = stringResource(R.string.settingsActivity_tile_dataAndPrivacy_title),
                    icon = Icons.Outlined.Shield,
                    activity = DataSettingsActivity::class.java
                )
            }
            // Accessibility
            item {
                TileStartActivity(
                    title = stringResource(R.string.settingsActivity_tile_accessibility_title),
                    icon = Icons.Outlined.AccessibilityNew,
                    activity = AccessibilitySettingsActivity::class.java
                )
            }
            item { HorizontalDivider() }

            // About
            item { TileHeader(stringResource(R.string.settingsActivity_tile_about_headerTitle)) }
            // Changelog
            item {
                analytics?.logEvent("opened_changelog_inAppLink", null)
                TileClick(
                    title = stringResource(R.string.settingsActivity_tile_changelog_title),
                    icon = Icons.Outlined.NewReleases,
                ) {
                    if (activity != null) {
                        openChromeCustomTab(activity, remoteConfig.getString("changelog_url"))
                    }
                }
            }
            // OpenPlayStorePage
            item {
                TileClick(
                    title = stringResource(R.string.settingsActivity_tile_openPlayStorePage_title),
                    icon = Icons.Outlined.StarOutline,
                ) {
                    analytics?.logEvent("opened_playStore_inAppLink", null)

                    if (activity != null) {
                        openPlayStoreUrl(activity, remoteConfig.getString("play_store_url"))
                    }
                }
            }
            // DiscordInvite
            item {
                TileClick(
                    title = stringResource(R.string.settingsActivity_tile_discordInvite_title),
                    description = stringResource(R.string.settingsActivity_tile_discordInvite_secondary),
                    icon = Icons.Outlined.Forum,
                ) {
                    analytics?.logEvent("opened_discord_inAppLink", null)

                    if (activity != null)  {
                        openChromeCustomTab(
                            activity,
                            remoteConfig.getString("discord_invite")
                        )
                    }
                }
            }
            // Feedback
            item {
                TileClick(
                    title = stringResource(R.string.settingsActivity_tile_feedback_title),
                    description = stringResource(R.string.settingsActivity_tile_feedback_secondary),
                    icon = Icons.Outlined.Feedback,
                ) {
                    activity?.startActivity(
                        Intent(activity, FeedbackActivity::class.java).putExtra(
                            "screenName",
                            "SettingsActivity"
                        )
                    )
                }
            }


            if (showDebug) {
                item { HorizontalDivider() }

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

                item {
                    TileClick(
                        title = "Firebase App Installation ID",
                        description = CountersApplication.firebaseInstallationID ?: "Error",
                        icon = Icons.Outlined.Tag
                    ) {
                        if (CountersApplication.firebaseInstallationID != null) {
                            clipboard.nativeClipboard.setPrimaryClip(
                                ClipData.newPlainText(
                                    "RahNeil_N3:CountersOld",
                                    CountersApplication.firebaseInstallationID!!
                                )
                            )
                        }
                    }
                }
            }
            item {
                Box(Modifier.navigationBarsPadding())
            }
        }
    }
}