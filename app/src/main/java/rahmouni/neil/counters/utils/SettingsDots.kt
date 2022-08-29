package rahmouni.neil.counters.utils

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import rahmouni.neil.counters.R
import rahmouni.neil.counters.utils.feedback.FeedbackActivity

@Composable
fun SettingsDots(
    feedback: Boolean = true,
    screenName: String,
    feedbackDivider: Boolean = false,
    content: @Composable () -> Unit,
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(false) }

    // MoreOptions
    IconButton(
        modifier = Modifier.testTag("SETTINGS_DOTS"),
        onClick = {
            localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
            expanded = true
        }) {
        Icon(
            Icons.Outlined.MoreVert,
            contentDescription = stringResource(R.string.settingsDots_icon_moreOptions_contentDescription)
        )
    }
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { expanded = false }
    ) {
        content()

        if (feedback) {
            if (feedbackDivider) Divider()

            // Feedback
            DropdownMenuItem(
                text = { Text(stringResource(R.string.settingsDots_dropdownMenuItem_feedback_text)) },
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    context.startActivity(
                        Intent(context, FeedbackActivity::class.java).putExtra(
                            "screenName",
                            screenName
                        )
                    )
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