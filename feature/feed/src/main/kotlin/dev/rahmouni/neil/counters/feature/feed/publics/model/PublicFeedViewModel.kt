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

package dev.rahmouni.neil.counters.feature.feed.publics.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.data.model.toEntity
import dev.rahmouni.neil.counters.core.data.repository.friendData.FriendsDataRepository
import dev.rahmouni.neil.counters.core.data.repository.publicFeedData.PublicFeedDataRepository
import dev.rahmouni.neil.counters.core.data.repository.userData.UserDataRepository
import dev.rahmouni.neil.counters.feature.feed.model.toEntity
import dev.rahmouni.neil.counters.feature.feed.publics.model.PublicFeedUiState.Loading
import dev.rahmouni.neil.counters.feature.feed.publics.model.PublicFeedUiState.Success
import dev.rahmouni.neil.counters.feature.feed.publics.model.data.PublicFeedData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class PublicFeedViewModel @Inject constructor(
    authHelper: AuthHelper,
    userDataRepository: UserDataRepository,
    private val friendsDataRepository: FriendsDataRepository,
    private val publicFeedDataRepository: PublicFeedDataRepository,
) : ViewModel() {

    val uiState: StateFlow<PublicFeedUiState> =
        combine(
            userDataRepository.userData,
            friendsDataRepository.userFriends,
            publicFeedDataRepository.userPublicPosts,
            authHelper.getUserFlow(),
        ) { userData, friends, posts, user ->
            Success(
                PublicFeedData(
                    user = user,
                    address = userData.address,
                    phone = userData.phone,
                    friends = friends.map { it.toEntity() },
                    posts = posts.map { it.toEntity() },
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )
}
