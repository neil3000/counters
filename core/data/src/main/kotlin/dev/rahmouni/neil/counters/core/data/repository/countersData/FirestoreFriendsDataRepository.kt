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

package dev.rahmouni.neil.counters.core.data.repository.countersData

import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dev.rahmouni.neil.counters.core.analytics.AnalyticsHelper
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.data.model.FriendRawData
import dev.rahmouni.neil.counters.core.data.repository.logCreatedCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject

class FirestoreFriendsDataRepository @Inject constructor(
    private val analyticsHelper: AnalyticsHelper,
    private val authHelper: AuthHelper,
) : FriendsDataRepository {

    override val userFriends: Flow<List<FriendRawData>> =
        authHelper.getUserFlow().transformLatest { user ->
            emitAll(
                try {
                    Firebase.firestore
                        .collection("friends")
                        .whereEqualTo(
                            FriendRawData::ownerUserUid.name,
                            user.getUid(),
                        )
                        .dataObjects<FriendRawData>()
                } catch (e: Exception) {
                    throw e
                    // TODO Feedback error
                },
            )
        }

    override fun addFriend(friendRawData: FriendRawData) {
        Firebase.firestore
            .collection("friends")
            .add(
                friendRawData.copy(
                    ownerUserUid = authHelper.getUser().getUid(),
                ),
            )

        analyticsHelper.logCreatedCounter()
    }
}
