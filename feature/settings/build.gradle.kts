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

@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.rn3.android.feature)
    alias(libs.plugins.rn3.android.library.compose)
    alias(libs.plugins.rn3.android.library.jacoco)
}

android {
    namespace = "dev.rahmouni.neil.counters.feature.settings"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(libs.androidx.compose.material.iconsExtended)

    api(projects.core.config)
    api(projects.core.data)
    api(projects.core.feedback) // Only in settings!
    api(projects.core.ui) // Only in settings!

    implementation(libs.androidx.appcompat)
    implementation(libs.coil.kt.compose)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.config)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}
