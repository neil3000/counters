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

package dev.rahmouni.neil.counters.feature.connect.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.data.model.FriendRawData
import dev.rahmouni.neil.counters.core.data.model.toEntity
import dev.rahmouni.neil.counters.core.data.repository.friendData.FriendsDataRepository
import dev.rahmouni.neil.counters.core.data.repository.userData.UserDataRepository
import dev.rahmouni.neil.counters.feature.connect.model.ConnectUiState.Loading
import dev.rahmouni.neil.counters.feature.connect.model.ConnectUiState.Success
import dev.rahmouni.neil.counters.feature.connect.model.data.ConnectData
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class ConnectViewModel @Inject constructor(
    authHelper: AuthHelper,
    userDataRepository: UserDataRepository,
    private val friendsDataRepository: FriendsDataRepository,
) : ViewModel() {

    fun addFriend(friendRawData: FriendRawData) {
        friendsDataRepository.addFriend(friendRawData)
    }


    val uiState: StateFlow<ConnectUiState> =
        combine(
            userDataRepository.userData,
            friendsDataRepository.userFriends,
            authHelper.getUserFlow(),
        ) { userData, friends, user ->
            Success(
                ConnectData(
                    user = user,
                    address = userData.address,
                    phone = userData.phone,
                    friends = friends.sortedWith(compareBy<FriendRawData> { it.nearby }.thenBy { it.name })
                        .map { it.toEntity() },
                ),
            )
        }.stateIn(
            scope = viewModelScope,
            initialValue = Loading,
            started = WhileSubscribed(5.seconds.inWholeMilliseconds),
        )
}
