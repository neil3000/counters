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

package dev.rahmouni.neil.counters.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.rahmouni.neil.counters.core.data.repository.countersData.CountersDataRepository
import dev.rahmouni.neil.counters.core.data.repository.countersData.FirestoreCountersDataRepository
import dev.rahmouni.neil.counters.core.data.repository.linksRn3UrlData.FirestoreLinksRn3UrlDataRepository
import dev.rahmouni.neil.counters.core.data.repository.linksRn3UrlData.LinksRn3UrlDataRepository
import dev.rahmouni.neil.counters.core.data.repository.userData.OfflineFirstUserDataRepository
import dev.rahmouni.neil.counters.core.data.repository.userData.UserDataRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    internal abstract fun bindsUserDataRepository(
        userDataRepository: OfflineFirstUserDataRepository,
    ): UserDataRepository

    @Binds
    internal abstract fun bindsCounterDataRepository(
        countersDataRepository: FirestoreCountersDataRepository,
    ): CountersDataRepository

    @Binds
    internal abstract fun bindsLinksRn3UrlDataRepository(
        linksRn3UrlDataRepository: FirestoreLinksRn3UrlDataRepository,
    ): LinksRn3UrlDataRepository
}
