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

package dev.rahmouni.neil.counters.core.model.data

enum class Country(val isoCode: String, val phoneCode: Int) {
    FRANCE(isoCode = "FR", phoneCode = 33),
    BELGIUM(isoCode = "BE", phoneCode = 32),
    UNITED_KINGDOM(isoCode = "GB", phoneCode = 44),
    USA(isoCode = "US", phoneCode = 1);

    companion object {
        private val isoMap by lazy { entries.associateBy(Country::isoCode) }
        private val phoneMap by lazy { entries.associateBy(Country::phoneCode) }

        fun getCountryFromIso(isoCode: String?): Country? = isoMap[isoCode]
        fun getCountryFromPhoneCode(phoneCode: Int?): Country? = phoneMap[phoneCode]
    }
}
