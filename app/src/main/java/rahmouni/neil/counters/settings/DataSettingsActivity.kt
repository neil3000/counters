package rahmouni.neil.counters.settings

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.material.icons.outlined.Analytics
import androidx.compose.material.icons.outlined.ArrowDownward
import androidx.compose.material.icons.outlined.ArrowOutward
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.RestartAlt
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.core.view.WindowCompat
import com.google.firebase.analytics.logEvent
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import rahmouni.neil.counters.BuildConfig
import rahmouni.neil.counters.CountersApplication.Companion.analytics
import rahmouni.neil.counters.R
import rahmouni.neil.counters.database.CountersDatabase
import rahmouni.neil.counters.prefs
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.dialogs.ConfirmationDialog
import rahmouni.neil.counters.utils.openChromeCustomTab
import rahmouni.neil.counters.utils.tiles.TileClick
import rahmouni.neil.counters.utils.tiles.TileHeader
import rahmouni.neil.counters.utils.tiles.TileStartActivity
import rahmouni.neil.counters.utils.tiles.TileSwitch
import java.io.FileInputStream

class DataSettingsActivity : ComponentActivity() {

    private val TAG = "DataSettingsActivity"

    private val createDocumentLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.data?.let { uri ->
                    // Perform the copy operation in a background thread
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val dbInstance = CountersDatabase.getInstance(applicationContext)

                            // Force a WAL checkpoint to ensure all data is written to the main DB file.
                            // This is crucial for getting a consistent and complete database copy.
                            // The query PRAGMA wal_checkpoint(FULL) attempts to checkpoint all data.
                            // It returns a row with three integers: busy (0 or 1), log (pages in WAL), checkpointed (pages checkpointed).
                            dbInstance.openHelper.writableDatabase.query("PRAGMA wal_checkpoint(FULL)").use { cursor ->
                                if (cursor.moveToFirst()) {
                                    val busy = cursor.getInt(0)
                                    val logSize = cursor.getInt(1)
                                    val checkpointedPages = cursor.getInt(2)
                                    Log.d(TAG, "WAL Checkpoint result: busy=$busy, logPages=$logSize, checkpointedPages=$checkpointedPages")
                                } else {
                                    Log.w(TAG, "WAL Checkpoint PRAGMA did not return any rows.")
                                }
                            }

                            val dbFile = applicationContext.getDatabasePath("counters_database")
                            Log.d(TAG, "Database path: ${dbFile.absolutePath}, Exists: ${dbFile.exists()}, Size: ${dbFile.length()} bytes")

                            if (dbFile.exists() && dbFile.length() > 0) {
                                contentResolver.openOutputStream(uri)?.use { outputStream ->
                                    FileInputStream(dbFile).use { inputStream ->
                                        val bytesCopied = inputStream.copyTo(outputStream)
                                        Log.d(TAG, "Bytes copied: $bytesCopied to URI: $uri")
                                        if (bytesCopied == 0L && dbFile.length() > 0) {
                                            Log.w(TAG, "Warning: 0 bytes copied even though source file has content. Check stream operations.")
                                        }
                                    }
                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(this@DataSettingsActivity, "Database exported successfully", Toast.LENGTH_SHORT).show()
                                    }
                                    analytics?.logEvent("database_exported") {
                                        param("file_size", dbFile.length())
                                    }
                                } ?: run {
                                    val errorMsg = "Could not open output stream for URI: $uri"
                                    Log.e(TAG, errorMsg)
                                    throw Exception(errorMsg)
                                }
                            } else {
                                val errorMsg = "Database file not found or is empty at path: ${dbFile.absolutePath}. Size: ${dbFile.length()}"
                                Log.e(TAG, errorMsg)
                                throw Exception(errorMsg)
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, "Error exporting database", e)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@DataSettingsActivity, "Error exporting database", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            } else {
                Log.d(TAG, "Export cancelled by user or failed: resultCode=${result.resultCode}")
                if (result.resultCode != RESULT_CANCELED) {
                    Toast.makeText(this@DataSettingsActivity, "Export operation failed or was cancelled", Toast.LENGTH_SHORT).show()
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
            item {
                TileClick(
                    title = "Export database",
                    icon = Icons.Outlined.ArrowOutward,
                    onClick = {
                        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onExportDatabase()
                    }
                )
                TileStartActivity(
                    title = "Import database",
                    icon = Icons.Outlined.ArrowDownward,
                    enabled = false,
                    activity = null
                )
            }

            item { TileHeader("Privacy") }
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
                        it() // it() is the showDialog lambda from ConfirmationDialog
                    }
                }
            }
            item {
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