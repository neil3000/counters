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

package dev.rahmouni.neil.counters.feature.settings.developer.links.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlRawData
import dev.rahmouni.neil.counters.core.data.repository.linksRn3UrlData.LinksRn3UrlDataRepository
import dev.rahmouni.neil.counters.feature.settings.developer.links.model.data.DeveloperSettingsLinksData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class DeveloperSettingsLinksViewModel @Inject constructor(
    private val linksRn3UrlDataRepository: LinksRn3UrlDataRepository,
) : ViewModel() {

    val uiState: StateFlow<DeveloperSettingsLinksUiState> =
        linksRn3UrlDataRepository.links.map { links ->
            DeveloperSettingsLinksUiState(
                developerSettingsLinksData = DeveloperSettingsLinksData(
                    links = links,
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = DeveloperSettingsLinksUiState(
                DeveloperSettingsLinksData(
                    links = emptyList(),
                ),
            ),
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )

    fun favoriteLink(linkRn3UrlRawData: LinkRn3UrlRawData, favorite: Boolean) =
        linksRn3UrlDataRepository.setLink(linkRn3UrlRawData.copy(favorite = favorite))

    fun setLink(linkRn3UrlRawData: LinkRn3UrlRawData) =
        linksRn3UrlDataRepository.setLink(linkRn3UrlRawData)

    fun deleteLink(path: String) = linksRn3UrlDataRepository.deleteLink(path)
}
