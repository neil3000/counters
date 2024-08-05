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

package dev.rahmouni.neil.counters.core.data.model

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import dev.rahmouni.neil.counters.core.model.data.Country
import dev.rahmouni.neil.counters.core.model.data.PhoneInfo

@Keep
data class FriendRawData(
    @DocumentId val uid: String? = null,
    val ownerUserUid: String = "RahNeil_N3:error:xTdZVv31n9S4fjOB0dFtJBk2ZZR6Ch5F",
    val name: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val phoneCode: String? = null,
    val nearby: Boolean = false,
)
