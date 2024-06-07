/*
 * Copyright 2024 Rahmouni Neïl
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

package dev.rahmouni.neil.counters.feature.settings.developer.links.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlRawData
import dev.rahmouni.neil.counters.core.data.repository.LinksRn3UrlDataRepository
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
