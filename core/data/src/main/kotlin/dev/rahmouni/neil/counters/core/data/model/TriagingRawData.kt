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

package dev.rahmouni.neil.counters.core.data.model

import androidx.annotation.Keep
import com.google.firebase.firestore.DocumentId

@Keep
data class TriagingRawData(
    @DocumentId val uid: String? = null,
    val ownerUserUid: String = "RahNeil_N3:error:xTdZVv31n9S4fjOB0dFtJBk2ZZR6Ch5F",
    val text: String? = null,
    val analysed: Boolean = false,
    val feed: String? = null,
    val scope: String? = null,
    val analyse: String? = null,
    val type: String? = null,
)
