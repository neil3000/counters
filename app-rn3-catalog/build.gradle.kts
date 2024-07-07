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
import dev.rahmouni.neil.counters.FlavorDimension
import dev.rahmouni.neil.counters.Rn3Flavor

plugins {
    alias(libs.plugins.rn3.android.application)
    alias(libs.plugins.rn3.android.application.compose)
}

android {
    defaultConfig {
        applicationId = "dev.rahmouni.neil.rn3catalog"
        versionCode = 1
        versionName = "1.0.0" // X.Y.Z; X = Major, Y = minor, Z = Patch level

        // The UI catalog does not depend on content from the app, however, it depends on modules
        // which do, so we must specify a default value for the contentType dimension.
        missingDimensionStrategy(FlavorDimension.contentType.name, Rn3Flavor.demo.name)
    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
    namespace = "dev.rahmouni.neil.rn3catalog"

    buildTypes {
        release {
            // To publish on the Play store a private signing key is required, but to allow anyone
            // who clones the code to sign and run the release variant, use the debug signing key.
            // TODO: Abstract the signing configuration to a separate file to avoid hardcoding this.
            signingConfig = signingConfigs.named("debug").get()
        }
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)

    implementation(projects.core.common)
    implementation(projects.core.designsystem)
    implementation(projects.core.ui)
}

dependencyGuard {
    configuration("releaseRuntimeClasspath")
}
