plugins {
    alias(libs.plugins.rn3.android.library)
    alias(libs.plugins.rn3.android.library.compose)
}

android {
    namespace = "dev.rahmouni.neil.counters.core.shapes"
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.graphics.shapes.android)
}
