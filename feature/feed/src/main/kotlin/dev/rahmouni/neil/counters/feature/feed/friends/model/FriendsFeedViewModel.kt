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

package dev.rahmouni.neil.counters.feature.feed.friends.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.data.model.toEntity
import dev.rahmouni.neil.counters.core.data.repository.countersData.FriendsDataRepository
import dev.rahmouni.neil.counters.core.data.repository.userData.UserDataRepository
import dev.rahmouni.neil.counters.feature.feed.friends.model.FriendsFeedUiState.Loading
import dev.rahmouni.neil.counters.feature.feed.friends.model.FriendsFeedUiState.Success
import dev.rahmouni.neil.counters.feature.feed.friends.model.data.FriendsFeedData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class FriendsFeedViewModel @Inject constructor(
    authHelper: AuthHelper,
    userDataRepository: UserDataRepository,
    private val friendsDataRepository: FriendsDataRepository,
) : ViewModel() {

    val uiState: StateFlow<FriendsFeedUiState> =
        combine(
            userDataRepository.userData,
            friendsDataRepository.userFriends,
            authHelper.getUserFlow(),
        ) { userData, friends, user ->
            Success(
                FriendsFeedData(
                    user = user,
                    address = userData.address,
                    phone = userData.phone,
                    friends = friends.map { it.toEntity() },
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )
}
