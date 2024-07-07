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

import dev.rahmouni.neil.counters.feature.aboutme.model.data.PfpData.LocalImage
import dev.rahmouni.neil.counters.feature.aboutme.model.data.PfpData.RemoteImage

sealed interface PfpData {
    data object LocalImage : PfpData
    data class RemoteImage(val url: String) : PfpData
}

internal fun String.toPfpData(): PfpData {
    return when (this) {
        "LOCAL_IMAGE" -> LocalImage
        else -> RemoteImage(this)
    }
}
