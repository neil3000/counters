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

package dev.rahmouni.neil.counters.feature.aboutme.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.core.common.Rn3Uri.AndroidPreview
import dev.rahmouni.neil.counters.core.common.Rn3Uri.InMaintenance
import dev.rahmouni.neil.counters.core.common.Rn3Uri.SoonAvailable
import dev.rahmouni.neil.counters.core.common.Rn3Uri.Unavailable
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PfpData.LocalImage
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PreviewParameterData.aboutMeData_default
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PreviewParameterData.aboutMeData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [AboutMeData] for Composable previews.
 */
class AboutMeDataPreviewParameterProvider : PreviewParameterProvider<AboutMeData> {
    override val values: Sequence<AboutMeData> =
        sequenceOf(aboutMeData_default).plus(aboutMeData_mutations)
}

internal object PreviewParameterData {
    val aboutMeData_default = AboutMeData(
        pfp = LocalImage,
        bioShort = "Hi! I'm Neïl \uD83D\uDC4B\nI'm a 22 year old software engineering student, currently living in Paris \uD83E\uDD56",
        portfolioUri = AndroidPreview,
        socialLinks = SocialLink.getListFromConfigString("[{\"id\":\"instagram\",\"url\":\"https://www.instagram.com/neil_rahmouni\",\"tooltip\":\"Instagram\"},{\"id\":\"mastodon\",\"url\":\"https://mastodon.social/@neil_rahmouni@androiddev.social\",\"tooltip\":\"Mastodon\"},{\"id\":\"discord\",\"url\":\"https://discord.gg/4Y7EdE9kRY\",\"tooltip\":\"Discord Server\"},{\"id\":\"linkedin\",\"url\":\"https://www.linkedin.com/in/neil-rahmouni\",\"tooltip\":\"Linkedin\"},{\"id\":\"gitlab\",\"url\":\"https://gitlab.com/neil3000\",\"tooltip\":\"Gitlab\"}]"),
    )
    val aboutMeData_mutations = with(aboutMeData_default) {
        sequenceOf(
            copy(portfolioUri = InMaintenance),
            copy(portfolioUri = Unavailable),
            copy(portfolioUri = SoonAvailable),
        )
    }
}
