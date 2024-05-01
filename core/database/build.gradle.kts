plugins {
    alias(libs.plugins.rn3.android.library)
    alias(libs.plugins.rn3.android.library.jacoco)
    alias(libs.plugins.rn3.android.hilt)
    alias(libs.plugins.rn3.android.room)
}

android {
    defaultConfig {
        testInstrumentationRunner =
            "dev.rahmouni.neil.counters.core.testing.Rn3TestRunner"
    }
    namespace = "dev.rahmouni.neil.counters.core.database"
}

dependencies {
    api(projects.core.model)

    implementation(libs.kotlinx.datetime)

    androidTestImplementation(projects.core.testing)
}
