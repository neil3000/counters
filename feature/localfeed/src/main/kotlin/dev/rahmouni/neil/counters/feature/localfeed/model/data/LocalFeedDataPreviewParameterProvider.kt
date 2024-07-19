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

package dev.rahmouni.neil.counters.feature.localfeed.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.core.user.Rn3User.AnonymousUser
import dev.rahmouni.neil.counters.core.user.Rn3User.SignedInUser
import dev.rahmouni.neil.counters.feature.localfeed.model.data.PreviewParameterData.localFeedData_default
import dev.rahmouni.neil.counters.feature.localfeed.model.data.PreviewParameterData.localFeedData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [LocalFeedData] for Composable previews.
 */
class LocalFeedDataPreviewParameterProvider :
    PreviewParameterProvider<LocalFeedData> {
    override val values: Sequence<LocalFeedData> =
        sequenceOf(localFeedData_default).plus(localFeedData_mutations)
}

object PreviewParameterData {
    val localFeedData_default = LocalFeedData(
        user = SignedInUser(
            uid = "androidPreviewID",
            displayName = "Android Preview",
            pfpUri = null,
            isAdmin = false,
            email = "androidPreview@rahmouni.dev",
        ),
    )
    val localFeedData_mutations = with(localFeedData_default) {
        sequenceOf(
            copy(user = AnonymousUser("androidPreviewID")),
        )
    }
}
