package rahmouni.neil.counters.settings

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.BuildConfig
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.R
import rahmouni.neil.counters.prefs
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.dialogs.ConfirmationDialog
import rahmouni.neil.counters.utils.openChromeCustomTab
import rahmouni.neil.counters.utils.tiles.TileClick
import rahmouni.neil.counters.utils.tiles.TileSwitch

class DataSettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        FirebaseDynamicLinks.getInstance()
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

                Log.e("RahNeil_N3:Counters", deepLink.toString())
            }.addOnFailureListener { e -> Log.e("RahNeil_N3:Counters", e.toString()) }

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
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    var analyticsEnabled: Boolean? by rememberSaveable { mutableStateOf(prefs.analyticsEnabled) }
    var crashlyticsEnabled: Boolean? by rememberSaveable { mutableStateOf(prefs.crashlyticsEnabled) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.dataSettingsActivity_topbar_title)) },
                actions = {
                    SettingsDots(screenName = "DataSettingsActivity") {}
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
                            contentDescription = stringResource(R.string.dataSettingsActivity_topbar_icon_back_contentDescription)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxHeight()) {

            // AppMetrics
            item {
                TileSwitch(
                    title = stringResource(R.string.dataSettingsActivity_tile_appMetrics_title),
                    icon = Icons.Outlined.Analytics,
                    checked = analyticsEnabled ?: true && !BuildConfig.DEBUG,
                    enabled = !BuildConfig.DEBUG
                ) {
                    analytics?.logEvent("changed_settings") {
                        param("Analytics", it.toString())
                    }

                    analyticsEnabled = it
                    prefs.analyticsEnabled = it
                }
            }
            item {
                // ClearAppMetrics
                ConfirmationDialog(
                    title = stringResource(R.string.dataSettingsActivity_tile_clearAppMetrics_title),
                    body = { Text(stringResource(R.string.dataSettingsActivity_tile_clearAppMetrics_dialogMessage)) },
                    icon = Icons.Outlined.RestartAlt,
                    confirmLabel = stringResource(R.string.dataSettingsActivity_tile_clearAppMetrics_dialogConfirmation),
                    onConfirm = {
                        analytics?.logEvent("reset_analytics", null)

                        analytics?.resetAnalyticsData()
                    }
                ) {
                    TileClick(
                        stringResource(R.string.dataSettingsActivity_tile_clearAppMetrics_title),
                        Icons.Outlined.RestartAlt
                    ) {
                        it()
                    }
                }
            }
            item {
                // CrashReports
                TileSwitch(
                    title = stringResource(R.string.dataSettingsActivity_tile_crashReports_title),
                    description = stringResource(R.string.dataSettingsActivity_tile_crashReports_secondary),
                    icon = Icons.Outlined.BugReport,
                    checked = crashlyticsEnabled ?: true && !BuildConfig.DEBUG,
                    enabled = !BuildConfig.DEBUG
                ) {
                    analytics?.logEvent("changed_settings") {
                        param("Crashlytics", it.toString())
                    }

                    crashlyticsEnabled = it
                    prefs.crashlyticsEnabled = it
                }
            }
            item {
                TileClick(
                    title = stringResource(R.string.dataSettingsActivity_tile_privacyPolicy_title),
                    icon = Icons.Outlined.Policy,
                ) {
                    openChromeCustomTab(activity, remoteConfig.getString("privacy_policy_url"),)
                }
            }
        }
    }
}