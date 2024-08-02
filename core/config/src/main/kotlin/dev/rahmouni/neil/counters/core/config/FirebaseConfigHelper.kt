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
