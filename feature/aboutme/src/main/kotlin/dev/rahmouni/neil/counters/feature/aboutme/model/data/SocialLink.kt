/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.feature.aboutme.model.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Link
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import dev.rahmouni.neil.counters.core.common.Rn3Uri
import dev.rahmouni.neil.counters.core.common.toRn3Uri
import dev.rahmouni.neil.counters.core.designsystem.icons.Discord
import dev.rahmouni.neil.counters.core.designsystem.icons.Gitlab
import dev.rahmouni.neil.counters.core.designsystem.icons.Instagram
import dev.rahmouni.neil.counters.core.designsystem.icons.Linkedin
import dev.rahmouni.neil.counters.core.designsystem.icons.Mastodon
import dev.rahmouni.neil.counters.core.designsystem.icons.Threads
import org.json.JSONArray

class SocialLink(val id: String, val uri: Rn3Uri, val tooltip: String) {

    @Composable
    fun getIcon(): ImageVector {
        return when (this.id) {
            "discord" -> Icons.Outlined.Discord
            "gitlab" -> Icons.Outlined.Gitlab
            "instagram" -> Icons.Outlined.Instagram
            "linkedin" -> Icons.Outlined.Linkedin
            "mastodon" -> Icons.Outlined.Mastodon
            "threads" -> Icons.Outlined.Threads
            else -> Icons.Outlined.Link
        }
    }

    companion object {
        fun getListFromConfigString(configValue: String): List<SocialLink> {
            val jsonList = JSONArray(configValue)

            return (0 until jsonList.length()).map { jsonList.getJSONObject(it) }.map {
                SocialLink(
                    id = it.getString("id"),
                    uri = it.getString("url").toRn3Uri { },
                    tooltip = it.getString("tooltip"),
                )
            }
        }
    }
}
