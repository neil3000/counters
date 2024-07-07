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
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.config)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}
