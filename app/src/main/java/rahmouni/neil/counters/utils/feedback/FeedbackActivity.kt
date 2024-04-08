package rahmouni.neil.counters.utils.feedback

import android.app.Activity
import android.os.Build
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.BuildConfig
import rahmouni.neil.counters.CountersApplication
import rahmouni.neil.counters.R
import rahmouni.neil.counters.ui.theme.CountersTheme
import rahmouni.neil.counters.utils.sendEmail
import rahmouni.neil.counters.utils.tiles.TileDialogRadioButtons
import rahmouni.neil.counters.utils.tiles.TileHeader
import rahmouni.neil.counters.utils.tiles.TileSwitch

class FeedbackActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        Firebase.dynamicLinks.getDynamicLink(intent)

        setContent {
            CountersTheme {
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

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun FeedbackPage(previousScreen: String) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val activity = (LocalContext.current as Activity)
    val localHapticFeedback = LocalHapticFeedback.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    var feedbackType: FeedbackType? by rememberSaveable { mutableStateOf(null) }
    var feedbackLocation by rememberSaveable { mutableStateOf(FeedbackLocation.PREVIOUS_SCREEN) }
    var sendDeviceModel by rememberSaveable { mutableStateOf(true) }
    var sendAndroidVersion by rememberSaveable { mutableStateOf(true) }
    var sendFirebaseAppInstallationID by rememberSaveable { mutableStateOf(true) }
    var acceptContributorBadge by rememberSaveable { mutableStateOf(true) }
    var description by rememberSaveable { mutableStateOf("") }

    val device = if (MODEL.startsWith(MANUFACTURER, ignoreCase = true)) {
        MODEL
    } else {
        "$MANUFACTURER $MODEL"
    }
    val androidVersion = "${Build.VERSION.RELEASE} (${Build.VERSION.SDK_INT})"

    val canSend = feedbackType != null && description != ""

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(stringResource(R.string.feedbackActivity_topbar_title)) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                            activity.finish()
                        }
                    ) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = stringResource(R.string.feedbackActivity_topbar_icon_back_contentDescription)
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
                        Icons.AutoMirrored.Outlined.Send,
                        null,
                        Modifier.alpha(if (canSend) ContentAlpha.high else ContentAlpha.disabled)
                    )
                },
                containerColor = if (canSend) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                modifier = Modifier
                    .navigationBarsPadding(),
                onClick = {
                    if (canSend) {

                        sendEmail(
                            activity,
                            remoteConfig.getString("feedback_email"),
                            feedbackType!!.subject,
                            when (feedbackType!!) {
                                FeedbackType.BUG ->
                                    "VERSION: `${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})`\n\n" +
                                            "SCREEN: `${if (feedbackLocation == FeedbackLocation.PREVIOUS_SCREEN) previousScreen else "null"}`\n\n" +
                                            "DEVICE: `${if (sendDeviceModel) device else "null"}`\n\n" +
                                            "ANDROID_VERSION: `${if (sendDeviceModel) androidVersion else "null"}`\n\n" +
                                            "DESC:\n> $description\n\n" +
                                            "FIREBASE: `${if (sendFirebaseAppInstallationID) (CountersApplication.firebaseInstallationID ?: "Error") else "null"}`\n\n" +
                                            "CONTRIBUTOR: `$acceptContributorBadge`"
                                FeedbackType.FEATURE ->
                                    "VERSION: `${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})`\n\n" +
                                            "DESC:\n> $description\n\n" +
                                            "FIREBASE: `${if (sendFirebaseAppInstallationID) (CountersApplication.firebaseInstallationID ?: "Error") else "null"}`\n\n" +
                                            "CONTRIBUTOR: `$acceptContributorBadge`"
                                FeedbackType.TRANSLATION ->
                                    "VERSION: `${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})`\n\n" +
                                            "SCREEN: `${if (feedbackLocation == FeedbackLocation.PREVIOUS_SCREEN) previousScreen else "null"}`\n\n" +
                                            "DESC:\n> $description\n\n" +
                                            "FIREBASE: `${if (sendFirebaseAppInstallationID) (CountersApplication.firebaseInstallationID ?: "Error") else "null"}`\n\n" +
                                            "CONTRIBUTOR: `$acceptContributorBadge`"
                            }
                        )
                    }
                })
        }
    ) { innerPadding ->
        LazyColumn(contentPadding = innerPadding, modifier = Modifier.fillMaxHeight()) {
            item { TileHeader(stringResource(R.string.feedbackActivity_tile_general_headerTitle)) }
            item {
                // Category
                TileDialogRadioButtons(
                    title = stringResource(R.string.feedbackActivity_tile_category_title),
                    icon = Icons.Outlined.Category,
                    values = FeedbackType.values().toList(),
                    selected = feedbackType,
                    defaultSecondary = stringResource(R.string.feedbackActivity_tile_category_defaultSecondary)
                ) {
                    feedbackType = it as FeedbackType
                }
            }
            item {
                // Location
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
                Column(Modifier.padding(16.dp)) {
                    // Description
                    TextField(
                        label = { Text(stringResource(R.string.feedbackActivity_textField_description_label)) },
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
            item { Divider() }

            // AdditionalInfo
            item { TileHeader(stringResource(R.string.feedbackActivity_tile_additionalInfo_headerTitle)) }
            item {
                // DeviceModel
                TileSwitch(
                    title = stringResource(R.string.feedbackActivity_tile_deviceModel_title),
                    description = device,
                    icon = Icons.Outlined.PermDeviceInformation,
                    checked = feedbackType == FeedbackType.BUG && sendDeviceModel,
                    enabled = feedbackType == FeedbackType.BUG
                ) {
                    sendDeviceModel = it
                }
            }
            item {
                // AndroidVersion
                TileSwitch(
                    title = stringResource(R.string.feedbackActivity_tile_androidVersion_title),
                    description = androidVersion,
                    icon = Icons.Outlined.Android,
                    checked = feedbackType == FeedbackType.BUG && sendAndroidVersion,
                    enabled = feedbackType == FeedbackType.BUG
                ) {
                    sendAndroidVersion = it
                }
            }
            item {
                // Firebase
                TileSwitch(
                    title = stringResource(R.string.feedbackActivity_tile_firebase_title),
                    description = CountersApplication.firebaseInstallationID ?: "Error",
                    icon = Icons.Outlined.Tag,
                    checked = sendFirebaseAppInstallationID
                ) {
                    sendFirebaseAppInstallationID = it
                }
            }
            item {
                // ContributorBadge
                TileSwitch(
                    title = stringResource(R.string.feedbackActivity_tile_contributorBadge_title),
                    description = stringResource(R.string.feedbackActivity_tile_contributorBadge_description),
                    icon = Icons.Outlined.VolunteerActivism,
                    checked = acceptContributorBadge && sendFirebaseAppInstallationID,
                    enabled = sendFirebaseAppInstallationID
                ) {
                    acceptContributorBadge = it
                }
            }
        }
    }
}