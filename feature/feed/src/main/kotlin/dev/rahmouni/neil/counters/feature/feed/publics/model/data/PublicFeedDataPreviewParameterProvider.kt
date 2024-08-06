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

package dev.rahmouni.neil.counters.feature.feed.publics.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.rahmouni.neil.counters.core.model.data.AddressInfo
import dev.rahmouni.neil.counters.core.user.Rn3User.AnonymousUser
import dev.rahmouni.neil.counters.core.user.Rn3User.SignedInUser
import dev.rahmouni.neil.counters.feature.feed.publics.model.data.PreviewParameterData.publicFeedData_default
import dev.rahmouni.neil.counters.feature.feed.publics.model.data.PreviewParameterData.publicFeedData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [PublicFeedData] for Composable previews.
 */
class PublicFeedDataPreviewParameterProvider :
    PreviewParameterProvider<PublicFeedData> {
    override val values: Sequence<PublicFeedData> =
        sequenceOf(publicFeedData_default).plus(publicFeedData_mutations)
}

object PreviewParameterData {
    val publicFeedData_default = PublicFeedData(
        user = SignedInUser(
            uid = "androidPreviewID",
            displayName = "Android Preview",
            pfpUri = null,
            isAdmin = false,
            email = "androidPreview@rahmouni.dev",
        ),
        address = AddressInfo(
            country = null,
            region = null,
            locality = "",
            postalCode = null,
            street = "",
            auxiliaryDetails = null,
        ),
        phone = PhoneNumber(),
    )
    val publicFeedData_mutations = with(publicFeedData_default) {
        sequenceOf(
            copy(
                user = AnonymousUser(
                    uid = "androidPreviewID",
                ),
            ),
        )
    }
}
