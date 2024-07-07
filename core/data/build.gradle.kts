/*
 * Counters
 * Copyright (C) $year Rahmouni Neïl
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
 *c
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
plugins {
    alias(libs.plugins.rn3.android.library)
    alias(libs.plugins.rn3.android.library.jacoco)
    alias(libs.plugins.rn3.android.hilt)
    id("kotlinx-serialization")
}

android {
    namespace = "dev.rahmouni.neil.counters.core.data"
    @Suppress("UnstableApiUsage") //TODO remove when stable
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
}

dependencies {
    api(projects.core.analytics)
    api(projects.core.auth)
    api(projects.core.datastore)

    api(platform(libs.firebase.bom))
    api(libs.firebase.firestore)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.kotlinx.serialization.json)
    testImplementation(projects.core.datastoreTest)
    testImplementation(projects.core.testing)
}
