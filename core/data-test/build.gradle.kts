plugins {
    alias(libs.plugins.rn3.android.library)
    alias(libs.plugins.rn3.android.hilt)
}

android {
    namespace = "dev.rahmouni.neil.counters.core.data.test"
}

dependencies {
    api(projects.core.data)

    implementation(libs.hilt.android.testing)
}
