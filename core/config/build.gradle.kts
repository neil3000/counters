plugins {
    alias(libs.plugins.rn3.android.library)
    alias(libs.plugins.rn3.android.library.compose)
    alias(libs.plugins.rn3.android.hilt)
}

android {
    namespace = "dev.rahmouni.neil.counters.core.config"
}

dependencies {
    implementation(libs.androidx.compose.runtime)

    implementation(projects.core.designsystem)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.config)
}
