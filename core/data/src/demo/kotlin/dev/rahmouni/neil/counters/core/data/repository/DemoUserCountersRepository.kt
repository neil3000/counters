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

package dev.rahmouni.neil.counters.core.data.repository

import dev.rahmouni.neil.counters.core.data.model.UserCounter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.UUID
import javax.inject.Inject

internal class DemoUserCountersRepository @Inject constructor() : UserCountersRepository {

    override val userCounters: Flow<List<UserCounter>> = MutableStateFlow(
        listOf(
            UserCounter("id1", "Push-ups"),
            UserCounter("id2", "Coffee cups"),
        ),
    )

    override fun createUserCounter(title: String) {
        (userCounters as MutableStateFlow).compareAndSet(
            userCounters.value,
            userCounters.value.plus(
                UserCounter(
                    id = UUID.randomUUID().toString(),
                    title = title,
                ),
            ),
        )
    }
}
