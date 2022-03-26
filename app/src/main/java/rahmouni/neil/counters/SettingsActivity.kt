package rahmouni.neil.counters

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.BugReport
import androidx.compose.material.icons.outlined.Code
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.Switch

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
    val scrollBehavior = remember { TopAppBarDefaults.pinnedScrollBehavior() }
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current

    val showDebug = android.provider.Settings.Secure.getInt(
        activity.contentResolver,
        android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0
    ) != 0

    var debugMode: Boolean? by rememberSaveable { mutableStateOf(prefs.debugMode) }

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .statusBarsPadding(),
        topBar = {
            SmallTopAppBar(
                title = { Text(stringResource(R.string.text_settings)) },
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
        Column(Modifier.padding(innerPadding)) {
            ListItem(
                text = { androidx.compose.material.Text(stringResource(R.string.action_seeOnThePlayStore)) },
                icon = { Icon(Icons.Outlined.StarOutline, null) },
                modifier = Modifier
                    .clickable(
                        onClick = {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            val intent = Intent(Intent.ACTION_VIEW).apply {
                                data = Uri.parse(
                                    "https://play.google.com/store/apps/details?id=rahmouni.neil.counters"
                                )
                                setPackage("com.android.vending")
                            }
                            activity.startActivity(intent)
                        }
                    )
            )
            Divider()

            ListItem(
                text = { androidx.compose.material.Text(stringResource(R.string.action_reportBug)) },
                icon = { Icon(Icons.Outlined.BugReport, null) },
                modifier = Modifier
                    .clickable(
                        onClick = {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            val intent = Intent(Intent.ACTION_SENDTO).apply {
                                type = "message/rfc822"
                                data =
                                    Uri.parse("mailto:")
                                putExtra(
                                    Intent.EXTRA_EMAIL,
                                    arrayOf("contact-project+neil3000-counters-33617709-issue-@incoming.gitlab.com")
                                )
                                putExtra(Intent.EXTRA_SUBJECT, "Bug with...")
                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    "Try to explain the issue as well as steps to reproduce it.\nFeel free to add screenshots, and don't forget to edit the subject !"
                                )
                            }
                            activity.startActivity(intent)
                        }
                    )
            )
            Divider()

            if (showDebug) {
                ListItem(
                    text = { androidx.compose.material.Text("DEBUG_MODE") },
                    secondaryText = { androidx.compose.material.Text("For experimental users only\nShown because you have dev settings turned on\n\nREQUIRES RESTART") },
                    icon = { Icon(Icons.Outlined.Code, null) },
                    singleLineSecondaryText = false,
                    trailing = {
                        Switch(
                            checked = debugMode ?: false,
                            onCheckedChange = null,
                        )
                    },
                    modifier = Modifier
                        .toggleable(
                            value = debugMode ?: false
                        ) {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            prefs.debugMode = it
                            debugMode = it
                        }.padding(bottom=8.dp)
                )
                Divider()
            }
        }
    }
}