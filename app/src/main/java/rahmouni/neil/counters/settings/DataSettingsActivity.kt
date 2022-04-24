package rahmouni.neil.counters.settings

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.R
import rahmouni.neil.counters.prefs
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.tiles.TileConfirmation
import rahmouni.neil.counters.utils.tiles.TileOpenCustomTab
import rahmouni.neil.counters.utils.tiles.TileSwitch

class DataSettingsActivity : ComponentActivity() {
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
                            DataSettingsPage()
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun DataSettingsPage() {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    var analyticsEnabled: Boolean? by rememberSaveable { mutableStateOf(prefs.analyticsEnabled) }
    var crashlyticsEnabled: Boolean? by rememberSaveable { mutableStateOf(prefs.crashlyticsEnabled) }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .statusBarsPadding(),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.text_dataAndPrivacy)) },
                actions = {
                    SettingsDots {}
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
                TileSwitch(
                    title = stringResource(R.string.text_analytics),
                    icon = Icons.Outlined.ShowChart,
                    checked = analyticsEnabled?:true,
                ){
                    analytics?.logEvent("changed_settings") {
                        param("Analytics", it.toString())
                    }

                    analyticsEnabled = it
                    prefs.analyticsEnabled = it
                }
            }
            item {
                TileConfirmation(
                    title = stringResource(R.string.action_resetAnalyticsData),
                    icon = Icons.Outlined.RestartAlt,
                    message = stringResource(R.string.confirmation_resetAnalytics),
                    confirmString = stringResource(R.string.action_reset_short)
                ){
                    analytics?.logEvent("reset_analytics", null)

                    analytics?.resetAnalyticsData()
                }
            }
            item {
                TileSwitch(
                    title = stringResource(R.string.text_crashReports),
                    icon = Icons.Outlined.BugReport,
                    checked = crashlyticsEnabled?:true
                ){
                    analytics?.logEvent("changed_settings") {
                        param("Crashlytics", it.toString())
                    }

                    crashlyticsEnabled = it
                    prefs.crashlyticsEnabled = it
                }
            }
            item {
                TileOpenCustomTab(
                    title = stringResource(R.string.text_privacyPolicy),
                    icon = Icons.Outlined.Policy,
                    url = remoteConfig.getString("privacy_policy_url")
                )
            }
        }
    }
}