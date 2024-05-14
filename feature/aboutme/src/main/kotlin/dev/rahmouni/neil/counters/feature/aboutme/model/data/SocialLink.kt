package dev.rahmouni.neil.counters.feature.aboutme.model.data

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.core.net.toUri
import dev.rahmouni.neil.counters.core.designsystem.icons.Discord
import dev.rahmouni.neil.counters.core.designsystem.icons.Gitlab
import dev.rahmouni.neil.counters.core.designsystem.icons.Instagram
import dev.rahmouni.neil.counters.core.designsystem.icons.Linkedin
import dev.rahmouni.neil.counters.core.designsystem.icons.Mastodon
import org.json.JSONArray

class SocialLink(val id: String, val uri: Uri, val tooltip: String) {

    @Composable
    fun getIcon(): ImageVector {
        return when (this.id) {
            "instagram" -> Icons.Outlined.Instagram
            "mastodon" -> Icons.Outlined.Mastodon
            "discord" -> Icons.Outlined.Discord
            "linkedin" -> Icons.Outlined.Linkedin
            "gitlab" -> Icons.Outlined.Gitlab
            else -> Icons.Outlined.Link
        }
    }

    companion object {
        fun getListFromConfigString(configValue: String): List<SocialLink> {
            val jsonList = JSONArray(configValue)

            return (0 until jsonList.length()).map { jsonList.getJSONObject(it) }.map {
                SocialLink(
                    id = it.getString("id"),
                    uri = it.getString("url").toUri(),
                    tooltip = it.getString("tooltip"),
                )
            }
        }
    }
}