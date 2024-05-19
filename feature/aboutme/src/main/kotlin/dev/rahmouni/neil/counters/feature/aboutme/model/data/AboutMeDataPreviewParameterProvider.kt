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

object PreviewParameterData {
    val aboutMeData_default = AboutMeData(
        pfp = LocalImage,
        bioShort = "Hi! I'm Neïl \uD83D\uDC4B\nI'm a 22 year old software engineering student, currently living in Paris \uD83E\uDD56",
        portfolioUri = AndroidPreview,
        socialLinks = SocialLink.getListFromConfigString("[{\"id\":\"instagram\",\"url\":\"https://www.instagram.com/neil_rahmouni\",\"tooltip\":\"Instagram\"},{\"id\":\"mastodon\",\"url\":\"https://mastodon.social/@neil_rahmouni@androiddev.social\",\"tooltip\":\"Mastodon\"},{\"id\":\"discord\",\"url\":\"https://discord.gg/4Y7EdE9kRY\",\"tooltip\":\"Discord Server\"},{\"id\":\"linkedin\",\"url\":\"https://www.linkedin.com/in/neil-rahmouni\",\"tooltip\":\"Linkedin\"},{\"id\":\"gitlab\",\"url\":\"https://gitlab.com/neil3000\",\"tooltip\":\"Gitlab\"}]"),
    )
    val aboutMeData_mutations = sequenceOf(
        aboutMeData_default.copy(
            portfolioUri = InMaintenance,
        ),
        aboutMeData_default.copy(
            portfolioUri = Unavailable,
        ),
        aboutMeData_default.copy(
            portfolioUri = SoonAvailable,
        ),
    )
}
