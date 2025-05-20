package rahmouni.neil.counters.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.BuildConfig
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CountersDatabase // Assuming your Database class name
import rahmouni.neil.counters.prefs
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.dialogs.ConfirmationDialog
import rahmouni.neil.counters.utils.openChromeCustomTab
import rahmouni.neil.counters.utils.tiles.TileClick
import rahmouni.neil.counters.utils.tiles.TileHeader
import rahmouni.neil.counters.utils.tiles.TileStartActivity
import rahmouni.neil.counters.utils.tiles.TileSwitch
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class DataSettingsActivity : ComponentActivity() {

    // ActivityResultLauncher for creating a document (for database export)
    private val createDocumentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    try {
                        val dbFile = getDatabasePath("counters_database")
                        if (dbFile.exists()) {
                            contentResolver.openOutputStream(uri)?.use { outputStream ->
                                FileInputStream(dbFile).use { inputStream ->
                                    inputStream.copyTo(outputStream)
                                }
                                Toast.makeText(this, "Database exported successfully", Toast.LENGTH_SHORT).show()
                                analytics?.logEvent("database_exported", null)
                            } ?: throw Exception("Could not open output stream")
                        } else {
                            Toast.makeText(this, "Database file not found", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(this, "Error exporting database: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

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
                    DataSettingsPage(
                        onExportDatabase = {
                            val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
                                addCategory(Intent.CATEGORY_OPENABLE)
                                type = "application/vnd.sqlite3"
                                putExtra(Intent.EXTRA_TITLE, "counters_export.db")
                            }
                            createDocumentLauncher.launch(intent)
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataSettingsPage(onExportDatabase: () -> Unit = {}) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val activity = LocalActivity.current
    val localHapticFeedback = LocalHapticFeedback.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()
    val context = LocalContext.current

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
                            activity?.finish()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.dataSettingsActivity_topbar_icon_back_contentDescription)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxHeight()) {

            item { TileHeader("Imports & exports") }
            // Export
            item {
                TileClick(
                    title = "Export database",
                    icon = Icons.Outlined.ArrowOutward,
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onExportDatabase()
                    }
                )
                // Import
                TileStartActivity(
                    title = "Import database",
                    icon = Icons.Outlined.ArrowDownward,
                    enabled = false,
                    activity = null
                )
            }

            item { TileHeader("Privacy") }
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
                        Toast.makeText(context, "App metrics data cleared", Toast.LENGTH_SHORT).show()
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
                    if (activity != null) openChromeCustomTab(activity, remoteConfig.getString("privacy_policy_url"))
                }
            }
        }
    }
}