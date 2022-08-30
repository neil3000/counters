package rahmouni.neil.counters.utils.banner

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import rahmouni.neil.counters.R
import rahmouni.neil.counters.prefs
import rahmouni.neil.counters.utils.sendEmail

data class ContributeTranslateBannerData(
    val title: String? = null,
    val language: String? = null,
)

@Composable
fun ContributeTranslateBanner() {
    val localHapticFeedback = LocalHapticFeedback.current
    val remoteConfig = FirebaseRemoteConfig.getInstance()
    val activity = (LocalContext.current as Activity)

    val bannerData = Gson().fromJson(
        remoteConfig.getString("contribute_translate_banner"),
        ContributeTranslateBannerData::class.java
    )

    var bannerDismissed: Boolean by rememberSaveable { mutableStateOf(prefs.contributeTranslateBannerDismissed) }

    AnimatedVisibility(
        bannerData.title != null && !bannerDismissed,
        enter = expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically(shrinkTowards = Alignment.Top)
    ) {

        Banner(
            title = bannerData.title ?: "",
            description = stringResource(
                R.string.contributeTranslateBanner_banner_description,
                bannerData.language ?: ""
            ),
            icon = Icons.Outlined.Translate
        ) {
            // Dismiss
            TextButton({
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                bannerDismissed = true
                prefs.contributeTranslateBannerDismissed = true
            }, Modifier.padding(horizontal = 8.dp)) {
                Text(stringResource(R.string.contributeTranslateBanner_banner_button_dismiss_text))
            }

            // Start
            Button({
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                sendEmail(
                    activity,
                    remoteConfig.getString("feedback_email"),
                    "Want to help translate in " + bannerData.language
                )
            }) {
                Text(stringResource(R.string.contributeTranslateBanner_banner_button_start_text))
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Icon(Icons.Outlined.ArrowForward, null)
            }
        }
    }
}