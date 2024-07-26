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

package dev.rahmouni.neil.counters.feature.publication.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.core.user.AddressInfo
import dev.rahmouni.neil.counters.core.user.PhoneInfo
import dev.rahmouni.neil.counters.core.user.Rn3User.AnonymousUser
import dev.rahmouni.neil.counters.core.user.Rn3User.SignedInUser
import dev.rahmouni.neil.counters.feature.publication.model.data.PreviewParameterData.publicationData_default
import dev.rahmouni.neil.counters.feature.publication.model.data.PreviewParameterData.publicationData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [PublicationData] for Composable previews.
 */
class FriendsFeedDataPreviewParameterProvider :
    PreviewParameterProvider<PublicationData> {
    override val values: Sequence<PublicationData> =
        sequenceOf(publicationData_default).plus(publicationData_mutations)
}

object PreviewParameterData {
    val publicationData_default = PublicationData(
        user = SignedInUser(
            uid = "androidPreviewID",
            displayName = "Android Preview",
            pfpUri = null,
            isAdmin = false,
            email = "androidPreview@rahmouni.dev",
            address = AddressInfo(
                country = "United Kingdom",
                locality = "London",
                street = "221B Baker Street",
            ),
            phone = PhoneInfo(
                code = "44",
                number = "1234567890",
            ),
        ),
    )
    val publicationData_mutations = with(publicationData_default) {
        sequenceOf(
            copy(
                user = AnonymousUser(
                    uid = "androidPreviewID",
                    address = AddressInfo(
                        country = "United Kingdom",
                        locality = "London",
                        street = "221B Baker Street",
                    ),
                    phone = PhoneInfo(
                        code = "44",
                        number = "1234567890",
                    ),
                ),
            ),
        )
    }
}
