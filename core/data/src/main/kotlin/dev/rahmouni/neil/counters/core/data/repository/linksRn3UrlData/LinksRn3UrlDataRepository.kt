/*
 * Counters
 * Copyright (C) 2024 Rahmouni Neïl
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package dev.rahmouni.neil.counters.core.data.repository.linksRn3UrlData

import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlRawData
import kotlinx.coroutines.flow.Flow

interface LinksRn3UrlDataRepository {

    val links: Flow<List<LinkRn3UrlRawData>>
    fun setLink(linkRn3UrlRawData: LinkRn3UrlRawData)
    fun deleteLink(path: String)
}
