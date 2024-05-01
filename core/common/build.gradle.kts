plugins {
    alias(libs.plugins.rn3.android.library)
    alias(libs.plugins.rn3.android.library.jacoco)
    alias(libs.plugins.rn3.android.hilt)
}

android {
    namespace = "dev.rahmouni.neil.counters.core.common"
}

dependencies {
    implementation(libs.androidx.browser)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
}