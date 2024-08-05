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

package dev.rahmouni.neil.counters.core.model.data

enum class Country(val isoCode: String, val phoneCode: Int, val regex: String) {
    FRANCE(isoCode = "FR", phoneCode = 33, regex = "^0?[1-9]\\d{8}\$"),
    BELGIUM(isoCode = "BE", phoneCode = 32, regex = "^0?[1-9]\\d{8}\$"),
    UNITED_KINGDOM(isoCode = "GB", phoneCode = 44, regex = "^0?(\\d{9}|\\d{10})\$"),
    USA(isoCode = "US", phoneCode = 1, regex = "^\\d{10}\$");

    companion object {
        private val isoMap by lazy { entries.associateBy(Country::isoCode) }

        fun getCountryFromIso(isoCode: String?): Country? = isoMap[isoCode]
    }
}