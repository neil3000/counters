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
import androidx.compose.material.ListItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.statusBarsPadding
import rahmouni.neil.counters.ui.theme.CountersTheme

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

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .statusBarsPadding(),
        topBar = {
            SmallTopAppBar(
                title = { Text("Settings") }, //TODO i18n
                navigationIcon = {
                    IconButton(
                        onClick = {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            activity.finish()
                        }
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        ) //TODO i18n
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
        content = { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
                ListItem(
                    text = { androidx.compose.material.Text("See on the Play Store") }, //TODO i18n
                    icon = { Icon(Icons.Filled.StarOutline, null) },
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
                    text = { androidx.compose.material.Text("Report a bug") }, //TODO i18n
                    icon = { Icon(Icons.Filled.BugReport, null) },
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
                                    putExtra(Intent.EXTRA_SUBJECT, "Counters: Bug with...")
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
            }
        },
    )
}