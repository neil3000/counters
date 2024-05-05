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
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of `ConfigHelper` that fetches the value from the Firebase Remote Config backend.
 */
@Singleton
internal class StubConfigHelper @Inject constructor() : ConfigHelper {

    override fun init(activity: Activity) {}

    override fun getLastFetchStatus(): String {
        return "Not available in Preview"
    }

    override fun getBoolean(key: String): Boolean {
        return false
    }

    override fun getString(key: String): String {
        return "Not available in Preview"
    }

    override fun forEachEntry(action: ((key: String, value: String, source: String) -> Unit)) {}
}
