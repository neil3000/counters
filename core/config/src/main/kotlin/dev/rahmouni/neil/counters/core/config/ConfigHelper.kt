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

package dev.rahmouni.neil.counters.core.config

import android.app.Activity

interface ConfigHelper {
    fun init(activity: Activity)
    fun getLastFetchStatus(): String
    fun getBoolean(key: String): Boolean
    fun getString(key: String): String
    fun getRolloutFlag(id: Int): Boolean {
        return getBoolean("i_{id}")
    }

    fun forEachEntry(action: ((key: String, value: String, source: String) -> Unit))
}
