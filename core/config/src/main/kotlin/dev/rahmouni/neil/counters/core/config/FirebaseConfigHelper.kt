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
import android.text.format.DateUtils
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import javax.inject.Inject

/**
 * Implementation of `ConfigHelper` that fetches the value from the Firebase Remote Config backend.
 */
internal class FirebaseConfigHelper @Inject constructor(
    private val firebaseConfig: FirebaseRemoteConfig,
) : ConfigHelper {

    override fun init(activity: Activity) {
        firebaseConfig.setDefaultsAsync(R.xml.core_config_remote_config_defaults)
        firebaseConfig.fetchAndActivate()
    }

    @Suppress("SpellCheckingInspection")
    override fun getLastFetchStatus(): String {
        return when (firebaseConfig.info.lastFetchStatus) {
            FirebaseRemoteConfig.LAST_FETCH_STATUS_SUCCESS ->
                "SUCCESS (${
                    DateUtils.getRelativeTimeSpanString(
                        firebaseConfig.info.fetchTimeMillis,
                    )
                })"

            FirebaseRemoteConfig.LAST_FETCH_STATUS_FAILURE -> "FAILURE"
            FirebaseRemoteConfig.LAST_FETCH_STATUS_THROTTLED -> "THROTTLED"
            FirebaseRemoteConfig.LAST_FETCH_STATUS_NO_FETCH_YET -> "NO_FETCH_YET"
            else -> "RahNeil_N3:Error:NMdUsSOSmdgHuvcFuFr6WjorE25ZszWZ"
        }
    }

    override fun getBoolean(key: String): Boolean {
        return firebaseConfig.getBoolean(key)
    }

    override fun getString(key: String): String {
        return firebaseConfig.getString(key)
    }

    override fun forEachEntry(action: ((key: String, value: String, source: String) -> Unit)) {
        firebaseConfig.all.entries.forEach { (key, value) ->
            action(
                key,
                value.asString(),
                when (value.source) {
                    FirebaseRemoteConfig.VALUE_SOURCE_REMOTE -> "remote"
                    FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT -> "default"
                    else -> "static"
                },
            )
        }
    }
}
