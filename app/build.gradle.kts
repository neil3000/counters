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

import dev.rahmouni.neil.counters.Rn3BuildType

plugins {
    alias(libs.plugins.rn3.android.application)
    alias(libs.plugins.rn3.android.application.compose)
    alias(libs.plugins.rn3.android.application.flavors)
    alias(libs.plugins.rn3.android.application.jacoco)
    alias(libs.plugins.rn3.android.hilt)
    alias(libs.plugins.rn3.android.application.firebase)
    id("com.google.android.gms.oss-licenses-plugin")
    alias(libs.plugins.baselineprofile)
    alias(libs.plugins.roborazzi)
}

android {
    defaultConfig {
        applicationId = "rahmouni.neil.counters"
        versionCode = 730042
        versionName = "2.0.16" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        // Custom test runner to set up Hilt dependency graph
        testInstrumentationRunner = "dev.rahmouni.neil.counters.core.testing.Rn3TestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            applicationIdSuffix = Rn3BuildType.DEBUG.applicationIdSuffix
        }
        release {
            isMinifyEnabled = true
            applicationIdSuffix = Rn3BuildType.RELEASE.applicationIdSuffix
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )

            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.named("debug").get()
            // Ensure Baseline Profile is fresh for release builds.
            baselineProfile.automaticGenerationDuringBuild = true
        }
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    @Suppress("UnstableApiUsage") //TODO remove when stable
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
    namespace = "rahmouni.neil.counters"
}

dependencies {
    implementation(projects.feature.aboutme)
    implementation(projects.feature.dashboard)
    implementation(projects.feature.login)
    implementation(projects.feature.settings)

    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.metrics)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.play.app.update)
    implementation(libs.play.app.update.ktx)

    ksp(libs.hilt.compiler)

    debugImplementation(libs.androidx.compose.ui.testManifest)
    debugImplementation(projects.uiTestHiltManifest)

    kspTest(libs.hilt.compiler)

    testImplementation(projects.core.dataTest)
    testImplementation(projects.core.testing)
    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.androidx.work.testing)
    testImplementation(libs.hilt.android.testing)

    testDemoImplementation(libs.robolectric)
    testDemoImplementation(libs.roborazzi)
    testDemoImplementation(projects.core.screenshotTesting)

    androidTestImplementation(projects.core.testing)
    androidTestImplementation(projects.core.dataTest)
    androidTestImplementation(projects.core.datastoreTest)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.android.testing)
}

baselineProfile {
    // Don't build on every iteration of a full assemble.
    // Instead enable generation directly for the release build variant.
    automaticGenerationDuringBuild = false
}

dependencyGuard {
    configuration("prodReleaseRuntimeClasspath")
}