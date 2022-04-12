package rahmouni.neil.counters.utils.tiles

import android.app.Activity
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.clickable
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TileOpenCustomTab(
    title: String,
    icon: ImageVector,
    url: String
) {
    val localHapticFeedback = LocalHapticFeedback.current
    val activity = (LocalContext.current as Activity)

    ListItem(
        text = { Text(title) },
        icon = { Icon(icon, null) },
        modifier = Modifier
            .clickable(
                onClick = {
                    localHapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)

                    CustomTabsIntent.Builder().build().launchUrl(
                        activity,
                        Uri.parse(url)
                    )
                }
            )
    )
}