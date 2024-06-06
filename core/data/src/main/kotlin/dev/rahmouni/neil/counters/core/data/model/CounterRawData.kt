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

package dev.rahmouni.neil.counters.core.data.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

@Keep
data class CounterRawData(
    @DocumentId val uid: String? = null,
    val createdAt: Timestamp = Timestamp.now(),
    val currentValue: Long = 0,
    val currentValueComputedSegment: Timestamp? = null,
    val ownerUserUid: String = CounterRawDataDefaults.ownerUserUid,
    val title: String? = null,
)

@Suppress("ConstPropertyName")
object CounterRawDataDefaults {
    const val ownerUserUid = "noLastUserUid"
}

@Keep
data class IncrementRawData(
    @DocumentId val uid: String? = null,
    val value: Long = 1,
    val createdAt: Timestamp = Timestamp.now(),
    val description: String? = null,
)