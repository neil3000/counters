package rahmouni.neil.counters.utils.feedback

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.os.Bundle
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.accompanist.insets.ProvideWindowInsets
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.BuildConfig
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.tiles.TileDialogRadioButtons
import rahmouni.neil.counters.utils.tiles.TileSwitch

class FeedbackActivity : ComponentActivity() {
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
                            FeedbackPage(intent.getStringExtra("screenName") ?: "app_null")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun FeedbackPage(previousScreen: String) {
    val decayAnimationSpec = rememberSplineBasedDecay<Float>()
    val scrollBehavior = remember(decayAnimationSpec) {
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(decayAnimationSpec)
    }
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    var feedbackType: FeedbackType? by rememberSaveable { mutableStateOf(null) }
    var feedbackLocation by rememberSaveable { mutableStateOf(FeedbackLocation.PREVIOUS) }
    var sendDeviceModel by rememberSaveable { mutableStateOf(true) }
    var sendAndroidVersion by rememberSaveable { mutableStateOf(true) }
    var description by rememberSaveable { mutableStateOf("") }

    val device = if (MODEL.startsWith(MANUFACTURER, ignoreCase = true)) {
        MODEL
    } else {
        "$MANUFACTURER $MODEL"
    }
    val androidVersion = "${Build.VERSION.RELEASE} (${Build.VERSION.SDK_INT})"

    val canSend = feedbackType != null && description != ""

    Scaffold(
        modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .statusBarsPadding(),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.action_sendFeedback)) },
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
        floatingActionButton = {
            LargeFloatingActionButton(
                content = {
                    Icon(
                        Icons.Outlined.Send,
                        null,
                        Modifier.alpha(if (canSend) ContentAlpha.high else ContentAlpha.disabled)
                    )
                },
                containerColor = if (canSend) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .navigationBarsPadding(),
                onClick = {
                    if (canSend) {
                        val intent = Intent(Intent.ACTION_SENDTO).apply {
                            type = "message/rfc822"
                            data =
                                Uri.parse("mailto:")
                            putExtra(
                                Intent.EXTRA_EMAIL,
                                arrayOf(remoteConfig.getString("feedback_email"))
                            )
                            putExtra(Intent.EXTRA_SUBJECT, feedbackType!!.subject)

                            putExtra(
                                Intent.EXTRA_TEXT,
                                when (feedbackType!!) {
                                    FeedbackType.BUG ->
                                        "VERSION: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\n" +
                                                "SCREEN: ${if (feedbackLocation == FeedbackLocation.PREVIOUS) previousScreen else "null"}\n" +
                                                "DEVICE: ${if (sendDeviceModel) device else "null"}\n" +
                                                "ANDROID_VERSION: ${if (sendDeviceModel) androidVersion else "null"}\n" +
                                                "DESC:\n$description"
                                    FeedbackType.FEATURE ->
                                        "VERSION: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\n" +
                                                "DESC:\n$description"
                                    FeedbackType.TRANSLATION ->
                                        "VERSION: ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})\n" +
                                                "SCREEN: ${if (feedbackLocation == FeedbackLocation.PREVIOUS) previousScreen else "null"}\n" +
                                                "DESC:\n$description"
                                }
                            )
                        }

                        try {
                            activity.startActivity(intent)
                        } catch (e: ActivityNotFoundException) {
                            Toast.makeText(
                                activity,
                                activity.getString(R.string.error_noAppToSendEmails),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                })
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxHeight()) {
            item {
                TileDialogRadioButtons(
                    title = stringResource(R.string.text_category),
                    icon = Icons.Outlined.Category,
                    values = FeedbackType.values().toList(),
                    selected = feedbackType,
                    defaultSecondary = stringResource(R.string.action_selectACategory)
                ) {
                    feedbackType = it as FeedbackType
                }
            }
            item {
                TileDialogRadioButtons(
                    title = stringResource(feedbackType?.location ?: FeedbackType.BUG.location!!),
                    icon = Icons.Outlined.WebAsset,
                    values = FeedbackLocation.values().toList(),
                    selected = feedbackLocation,
                    enabled = if (feedbackType == null) false else feedbackType?.location != null
                ) {
                    feedbackLocation = it as FeedbackLocation
                }
            }
            item {
                TileSwitch(
                    title = stringResource(R.string.action_sendDeviceModel),
                    description = device,
                    icon = Icons.Outlined.PermDeviceInformation,
                    checked = feedbackType == FeedbackType.BUG && sendDeviceModel,
                    enabled = feedbackType == FeedbackType.BUG
                ) {
                    sendDeviceModel = it
                }
            }
            item {
                TileSwitch(
                    title = stringResource(R.string.action_sendAndroidVersion),
                    description = androidVersion,
                    icon = Icons.Outlined.Android,
                    checked = feedbackType == FeedbackType.BUG && sendAndroidVersion,
                    enabled = feedbackType == FeedbackType.BUG
                ) {
                    sendAndroidVersion = it
                }
            }
            item {
                Column(Modifier.padding(16.dp)) {
                    TextField(
                        label = { Text(stringResource(R.string.text_description)) },
                        value = description,
                        onValueChange = { description = it },
                        singleLine = false,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = stringResource(feedbackType?.describe ?: FeedbackType.BUG.describe),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                    )
                }
            }
        }
    }
}