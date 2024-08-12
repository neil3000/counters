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

package dev.rahmouni.neil.counters.core.config

import android.app.Activity
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

    override fun getBoolean(key: String): Boolean {
        return firebaseConfig.getBoolean(key)
    }

    override fun getString(key: String): String {
        return firebaseConfig.getString(key).replace(oldValue = "\\n", newValue = "\n")
    }
}
