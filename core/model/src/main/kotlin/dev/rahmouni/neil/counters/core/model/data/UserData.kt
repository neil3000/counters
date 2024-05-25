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

package dev.rahmouni.neil.counters.core.model.data

data class UserData(
    val hasAccessibilityEmphasizedSwitchesEnabled: Boolean,
    val hasAccessibilityIconTooltipsEnabled: Boolean,
    val hasMetricsEnabled: Boolean,
    val hasCrashlyticsEnabled: Boolean,

    /**
     * Remembers if the user has last enabled sync or not.
     * */
    val hasSyncEnabled: Boolean,

    /**
     * Remembers the last UID of the [SignedInUser][dev.rahmouni.neil.counters.core.auth.user.Rn3User.SignedInUser].
     *
     * • If never logged in this is `null`.
     *
     * *This is used to keep showing data from the local persistence of Firestore even after the user logs out.*
     * */
    val lastUserUid: String?,
)
