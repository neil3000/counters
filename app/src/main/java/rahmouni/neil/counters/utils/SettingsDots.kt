package rahmouni.neil.counters.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import rahmouni.neil.counters.R

@Composable
fun SettingsDots(
    feedback: Boolean = true,
    feedbackDivider: Boolean = false,
    content: @Composable () -> Unit,
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val activity = (LocalContext.current as Activity)
    val remoteConfig = FirebaseRemoteConfig.getInstance()

    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = {
        localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
        expanded = true
    }) {
        Icon(
            Icons.Outlined.MoreVert,
            contentDescription = stringResource(R.string.text_more_options)
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        content()

        if (feedback) {
            if (feedbackDivider) MenuDefaults.Divider()
            DropdownMenuItem(
                text = { Text(stringResource(R.string.action_sendFeedback)) },
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    val intent = Intent(Intent.ACTION_SENDTO).apply {
                        type = "message/rfc822"
                        data =
                            Uri.parse("mailto:")
                        putExtra(
                            Intent.EXTRA_EMAIL,
                            arrayOf(remoteConfig.getString("feedback_email"))
                        )
                    }
                    activity.startActivity(intent)
                },
                leadingIcon = {
                    Icon(
                        Icons.Outlined.Feedback,
                        contentDescription = null
                    )
                }
            )

        }
    }
}