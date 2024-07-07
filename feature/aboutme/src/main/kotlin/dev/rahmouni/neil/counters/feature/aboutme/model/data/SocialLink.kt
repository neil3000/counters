/*
 * Copyright 2024 Rahmouni Neïl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
