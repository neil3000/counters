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

package dev.rahmouni.neil.counters.core.data.test

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import dev.rahmouni.neil.counters.core.data.di.DataModule
import dev.rahmouni.neil.counters.core.data.repository.eventFeedData.EventFeedDataRepository
import dev.rahmouni.neil.counters.core.data.repository.eventFeedData.FirestoreEventFeedDataRepository
import dev.rahmouni.neil.counters.core.data.repository.friendData.FirestoreFriendsDataRepository
import dev.rahmouni.neil.counters.core.data.repository.friendData.FriendsDataRepository
import dev.rahmouni.neil.counters.core.data.repository.friendFeedData.FirestoreFriendFeedDataRepository
import dev.rahmouni.neil.counters.core.data.repository.friendFeedData.FriendFeedDataRepository
import dev.rahmouni.neil.counters.core.data.repository.linksRn3UrlData.FirestoreLinksRn3UrlDataRepository
import dev.rahmouni.neil.counters.core.data.repository.linksRn3UrlData.LinksRn3UrlDataRepository
import dev.rahmouni.neil.counters.core.data.repository.publicFeedData.FirestorePublicFeedDataRepository
import dev.rahmouni.neil.counters.core.data.repository.publicFeedData.PublicFeedDataRepository
import dev.rahmouni.neil.counters.core.data.repository.userData.UserDataRepository
import dev.rahmouni.neil.counters.core.data.test.repository.FakeUserDataRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class],
)
internal interface TestDataModule {
    @Binds
    fun bindsUserDataRepository(
        userDataRepository: FakeUserDataRepository,
    ): UserDataRepository

    @Binds
    fun bindsFriendsDataRepository(
        friendsDataRepository: FirestoreFriendsDataRepository,
    ): FriendsDataRepository

    @Binds
    fun bindsEventFeedDataRepository(
        eventFeedDataRepository: FirestoreEventFeedDataRepository,
    ): EventFeedDataRepository

    @Binds
    fun bindsFriendFeedDataRepository(
        friendFeedDataRepository: FirestoreFriendFeedDataRepository,
    ): FriendFeedDataRepository

    @Binds
    fun bindsPublicFeedDataRepository(
        publicFeedDataRepository: FirestorePublicFeedDataRepository,
    ): PublicFeedDataRepository

    @Binds
    fun bindsLinksRn3UrlDataRepository(
        linksRn3UrlDataRepository: FirestoreLinksRn3UrlDataRepository,
    ): LinksRn3UrlDataRepository
}
