package rahmouni.neil.counters.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.R
import rahmouni.neil.counters.prefs
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.feedback.FeedbackActivity
import rahmouni.neil.counters.utils.openChromeCustomTab
import rahmouni.neil.counters.utils.openPlayStoreUrl
import rahmouni.neil.counters.utils.sendEmail
import rahmouni.neil.counters.utils.tiles.*

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        Firebase.dynamicLinks.getDynamicLink(intent)

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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current
    val clipboard = LocalClipboardManager.current

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

                            activity.finish()
                        }
                    ) {
                        Icon(
                            Icons.Outlined.ArrowBack,
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
                    values = StartWeekDay.values().toList(),
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
                    values = WeekDisplay.values().toList(),
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
            item { Divider() }

            // About
            item { TileHeader(stringResource(R.string.settingsActivity_tile_about_headerTitle)) }
            if (remoteConfig.getBoolean("issue182__move_changelog")) {
                // Changelog
                item {
                    TileOpenCustomTab(
                        title = stringResource(R.string.settingsActivity_tile_changelog_title),
                        icon = Icons.Outlined.NewReleases,
                        url = remoteConfig.getString("changelog_url")
                    )
                }
            }
            // OpenPlayStorePage
            item {
                TileClick(
                    title = stringResource(R.string.settingsActivity_tile_openPlayStorePage_title),
                    icon = Icons.Outlined.StarOutline,
                ) {
                    openPlayStoreUrl(activity, remoteConfig.getString("play_store_url"))
                }
            }
            if (!remoteConfig.getBoolean("issue182__move_changelog")) {
                // Changelog
                item {
                    TileOpenCustomTab(
                        title = stringResource(R.string.settingsActivity_tile_changelog_title),
                        icon = Icons.Outlined.NewReleases,
                        url = remoteConfig.getString("changelog_url")
                    )
                }
            }
            if (!remoteConfig.getBoolean("issue185__remove_help_translate")) {
                // HelpTranslate
                item {
                    TileClick(
                        title = "Help translate the app",
                        icon = Icons.Outlined.Translate
                    ) {
                        sendEmail(
                            activity,
                            remoteConfig.getString("feedback_email"),
                            "Want to help translate"
                        )
                    }
                }
            }
            if (remoteConfig.getString("issue186__discord_invite") != "null") {
                // DiscordInvite
                item {
                    TileClick(
                        title = stringResource(R.string.settingsActivity_tile_discordInvite_title),
                        description = stringResource(R.string.settingsActivity_tile_discordInvite_secondary),
                        icon = Icons.Outlined.Forum,
                        singleLineSecondaryText = false
                    ) {
                        openChromeCustomTab(
                            activity,
                            remoteConfig.getString("issue186__discord_invite")
                        )
                    }
                }
            }
            if (remoteConfig.getString("issue195__feedback_tile") != "null") {
                // Feedback
                item {
                    TileClick(
                        title = stringResource(R.string.settingsActivity_tile_feedback_title),
                        description = stringResource(R.string.settingsActivity_tile_feedback_secondary),
                        icon = Icons.Outlined.Feedback,
                        singleLineSecondaryText = false
                    ) {
                        activity.startActivity(
                            Intent(activity, FeedbackActivity::class.java).putExtra(
                                "screenName",
                                "SettingsActivity"
                            )
                        )
                    }
                }
            }


            if (showDebug) {
                item { Divider() }

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
                            clipboard.setText(AnnotatedString(CountersApplication.firebaseInstallationID!!))
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