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

package dev.rahmouni.neil.counters.feature.publication.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.data.model.EventFeedRawData
import dev.rahmouni.neil.counters.core.data.model.PostRawData
import dev.rahmouni.neil.counters.core.data.model.TriagingRawData
import dev.rahmouni.neil.counters.core.data.repository.eventFeedData.EventFeedDataRepository
import dev.rahmouni.neil.counters.core.data.repository.friendFeedData.FriendFeedDataRepository
import dev.rahmouni.neil.counters.core.data.repository.publicFeedData.PublicFeedDataRepository
import dev.rahmouni.neil.counters.core.data.repository.triagingData.TriagingDataRepository
import dev.rahmouni.neil.counters.core.data.repository.userData.UserDataRepository
import dev.rahmouni.neil.counters.feature.publication.model.PublicationUiState.Loading
import dev.rahmouni.neil.counters.feature.publication.model.PublicationUiState.Success
import dev.rahmouni.neil.counters.feature.publication.model.data.PublicationData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PublicationViewModel @Inject constructor(
    authHelper: AuthHelper,
    userDataRepository: UserDataRepository,
    private val triagingDataRepository: TriagingDataRepository,
    private val publicFeedDataRepository: PublicFeedDataRepository,
    private val friendFeedDataRepository: FriendFeedDataRepository,
    private val eventFeedDataRepository: EventFeedDataRepository,
) : ViewModel() {

    fun addTriagingPost(triagingRawData: TriagingRawData) {
        triagingDataRepository.addTriagingPost(triagingRawData)
    }

    fun removeTriagingPost(triagingRawData: TriagingRawData) {
        triagingDataRepository.removeTriagingPost(triagingRawData)
    }

    fun addPublicPost(postRawData: PostRawData) {
        publicFeedDataRepository.addPublicPost(postRawData)
    }

    fun addFriendPost(friendRawData: PostRawData) {
        friendFeedDataRepository.addFriendPost(friendRawData)
    }

    fun addEventPost(eventFeedRawData: EventFeedRawData) {
        eventFeedDataRepository.addEventPost(eventFeedRawData)
    }

    val uiState: StateFlow<PublicationUiState> =
        combine(
            userDataRepository.userData,
            triagingDataRepository.userTriagingPosts,
            authHelper.getUserFlow(),
        ) { userData, posts, user ->
            Success(
                PublicationData(
                    user = user,
                    address = userData.address,
                    phone = userData.phone,
                    posts = posts.sortedBy { it.analysed }.map { it.toEntity() },
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )
}
