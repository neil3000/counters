package rahmouni.neil.counters.utils

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
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

data class ContributeTranslateBannerData(
    val title: String? = null,
    val language: String? = null,
)

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
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
        Card(
            onClick = {
                localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                sendEmail(activity, remoteConfig.getString("feedback_email"), "Want to help translate in " + bannerData.language)
            },
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
        ) {
            Box(Modifier.fillMaxWidth()) {
                IconButton(onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    bannerDismissed = true
                    prefs.contributeTranslateBannerDismissed = true
                }, Modifier.align(Alignment.TopEnd)) {
                    Icon(Icons.Outlined.Close, stringResource(R.string.action_dismiss))
                }
                Row(
                    Modifier.padding(16.dp),
                    Arrangement.spacedBy(24.dp),
                    Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        tonalElevation = -LocalAbsoluteTonalElevation.current
                    ) {
                        Icon(Icons.Outlined.Translate, null, Modifier.padding(12.dp))
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(bannerData.title ?: "", style = MaterialTheme.typography.titleLarge)
                        Text(
                            stringResource(R.string.banner_contributeTranslate, bannerData.language?:""),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}