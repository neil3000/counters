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
    namespace = "dev.rahmouni.neil.counters.feature.login"
}

dependencies {
    api(libs.androidx.compose.material.iconsExtended)

    implementation(libs.androidx.appcompat)
    implementation(libs.coil.kt.compose)

    implementation(projects.core.auth)
    implementation(projects.core.shapes)
}
