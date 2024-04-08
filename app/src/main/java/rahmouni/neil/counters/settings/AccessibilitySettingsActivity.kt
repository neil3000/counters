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
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Celebration
import androidx.compose.material.icons.outlined.ToggleOn
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
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import rahmouni.neil.counters.R
import rahmouni.neil.counters.prefs
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.SettingsDots
import rahmouni.neil.counters.utils.tiles.TileSwitch

class AccessibilitySettingsActivity : ComponentActivity() {
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
                androidx.compose.material.Surface {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        AccessibilitySettingsPage()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun AccessibilitySettingsPage() {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current

    var iconSwitchesEnabled: Boolean? by rememberSaveable { mutableStateOf(prefs.iconSwitchesEnabled) }
    var confettiDisabled: Boolean? by rememberSaveable { mutableStateOf(prefs.confettiDisabled) }

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.accessibilitySettingsActivity_topbar_title)) },
                actions = {
                    SettingsDots(screenName = "AccessibilitySettingsActivity") {}
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
                            contentDescription = stringResource(R.string.accessibilitySettingsActivity_topbar_icon_back_contentDescription)
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxHeight()) {

            // IconSwitches
            item {
                TileSwitch(
                    title = stringResource(R.string.accessibilitySettingsActivity_tile_iconSwitches_title),
                    description = stringResource(R.string.accessibilitySettingsActivity_tile_iconSwitches_secondary),
                    icon = Icons.Outlined.ToggleOn,
                    checked = iconSwitchesEnabled ?: false
                ) {
                    iconSwitchesEnabled = it
                    prefs.iconSwitchesEnabled = it
                }
            }

            // DisableConfetti
            item {
                TileSwitch(
                    title = stringResource(R.string.accessibilitySettingsActivity_tile_disableConfetti_title),
                    description = stringResource(R.string.accessibilitySettingsActivity_tile_disableConfetti_secondary),
                    icon = Icons.Outlined.Celebration,
                    checked = confettiDisabled ?: false
                ) {
                    confettiDisabled = it
                    prefs.confettiDisabled = it
                }
            }
        }
    }
}