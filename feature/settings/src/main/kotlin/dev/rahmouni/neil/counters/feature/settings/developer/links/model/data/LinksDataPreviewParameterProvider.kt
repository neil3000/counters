/*
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.feature.settings.developer.links.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlRawData
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.data.PreviewParameterData.linksData_default

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [DeveloperSettingsLinksData] for Composable previews.
 */
class LinksDataPreviewParameterProvider :
    PreviewParameterProvider<DeveloperSettingsLinksData> {
    override val values: Sequence<DeveloperSettingsLinksData> = sequenceOf(linksData_default)
}

object PreviewParameterData {
    val linksData_default = DeveloperSettingsLinksData(
        links = listOf(
            LinkRn3UrlRawData(
                path = "install",
                description = "",
                redirectUrl = "https://www.google.com",
                clicks = 120,
            ),
            LinkRn3UrlRawData(
                path = "changelog",
                description = "Changelog of the app",
                redirectUrl = "https://www.google.com",
                clicks = 28,
            ),
        ),
    )
}
