/*
 * Copyright (C) 2024 Rahmouni Neïl & Aurélien Coppée
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

package dev.rahmouni.neil.counters.feature.connect.model.data

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.rahmouni.neil.counters.core.data.model.FriendEntity
import dev.rahmouni.neil.counters.core.model.data.AddressInfo
import dev.rahmouni.neil.counters.core.model.data.Country
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
        phone = PhoneNumber(),
        friends = listOf(
            FriendEntity(
                uid = "4",
                name = "",
                email = "david.brown@example.com",
                phone = PhoneNumber().setCountryCode(Country.BELGIUM.phoneCode)
                    .setNationalNumber(123456789),
                nearby = true,
                userId = "",
            ),
            FriendEntity(
                uid = "5",
                name = "Eva Davis",
                email = "eva.davis@example.com",
                phone = PhoneNumber().setCountryCode(Country.BELGIUM.phoneCode)
                    .setNationalNumber(123456789),
                userId = "",
            ),
        ),
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
