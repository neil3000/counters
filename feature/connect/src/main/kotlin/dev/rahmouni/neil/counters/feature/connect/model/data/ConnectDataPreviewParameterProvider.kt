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

package dev.rahmouni.neil.counters.feature.connect.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import dev.rahmouni.neil.counters.core.data.model.FriendEntity
import dev.rahmouni.neil.counters.core.model.data.AddressInfo
import dev.rahmouni.neil.counters.core.model.data.Country
import dev.rahmouni.neil.counters.core.model.data.PhoneInfo
import dev.rahmouni.neil.counters.core.user.Rn3User.AnonymousUser
import dev.rahmouni.neil.counters.core.user.Rn3User.SignedInUser
import dev.rahmouni.neil.counters.feature.connect.model.data.PreviewParameterData.connectData_default
import dev.rahmouni.neil.counters.feature.connect.model.data.PreviewParameterData.connectData_mutations

/**
 * This [PreviewParameterProvider](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/PreviewParameterProvider)
 * provides list of [ConnectData] for Composable previews.
 */
class ConnectDataPreviewParameterProvider :
    PreviewParameterProvider<ConnectData> {
    override val values: Sequence<ConnectData> =
        sequenceOf(connectData_default).plus(connectData_mutations)
}

object PreviewParameterData {
    val connectData_default = ConnectData(
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
        phone = PhoneInfo(
            number = null,
            code = null,
        ),
        friends = listOf(
            FriendEntity(
                uid = "4",
                name = "David Brown",
                email = "david.brown@example.com",
                phone = PhoneInfo(
                    code = Country.FRANCE,
                    number = "0123456789"
                ),
                nearby = true
            ),
            FriendEntity(
                uid = "5",
                name = "Eva Davis",
                email = "eva.davis@example.com",
                phone = PhoneInfo(
                    code = Country.BELGIUM,
                    number = "0123456789"
                ),
            ),
        )
    )
    val connectData_mutations = with(connectData_default) {
        sequenceOf(
            copy(
                user = AnonymousUser(
                    uid = "androidPreviewID",
                ),
            ),
        )
    }
}
