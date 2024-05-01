
@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
    alias(libs.plugins.rn3.android.feature)
    alias(libs.plugins.rn3.android.library.compose)
    alias(libs.plugins.rn3.android.library.jacoco)
}

android {
    namespace = "dev.rahmouni.neil.counters.feature.aboutme"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    api(libs.androidx.compose.material.iconsExtended)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.graphics.shapes.android)
    implementation(libs.coil.kt.compose)

    implementation(projects.core.data)
    implementation(projects.core.feedback)
    implementation(projects.core.config)

    testImplementation(projects.core.testing)

    androidTestImplementation(projects.core.testing)
}
