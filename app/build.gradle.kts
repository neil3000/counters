plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("androidx.room")
    id("com.google.devtools.ksp")
}

android {
    namespace = "rahmouni.neil.counters"
    compileSdk = 35

    defaultConfig {
        applicationId = "rahmouni.neil.counters"
        minSdk = 26
        targetSdk = 35
        versionCode = 72
        versionName = "1.27"
        //resourceConfigurations += ["en", "en-rUS", "en-rGB", "fr", "zh", "zh-rCN"]

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.gson)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.config)
    implementation(libs.firebase.appcheck.playintegrity)
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.konfetti.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)


    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.common.java8)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.runtime.livedata)

    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    annotationProcessor(libs.androidx.room.compiler)

    implementation(libs.androidx.browser)

    /*
    implementation('androidx.compose.material3:material3-window-size-class:1.2.1')

    implementation 'com.google.accompanist:accompanist-insets-ui:0.34.0'

    // Kotlin components
    api 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0'
    api 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0'

    // UI
    implementation "androidx.constraintlayout:constraintlayout:2.1.4"
    implementation "com.google.android.material:material:1.11.0"

    // Testing
    testImplementation "junit:junit:4.13.2"
    androidTestImplementation('androidx.compose.ui:ui-test-junit4:1.6.5')
    androidTestImplementation "androidx.arch.core:core-testing:2.2.0"
    debugImplementation('androidx.compose.ui:ui-test-manifest:1.6.5')
    androidTestImplementation('androidx.test.espresso:espresso-core:3.5.1', {
        exclude group: 'com.android.support', module: 'support-annotations'
    })
    androidTestImplementation "androidx.test.ext:junit:1.1.5"

    implementation("androidx.navigation:navigation-compose:2.7.7")

    implementation 'androidx.browser:browser:1.8.0'

    implementation "com.github.PhilJay:MPAndroidChart:v3.1.0"

    implementation "androidx.appcompat:appcompat:1.6.1"

    implementation "androidx.tracing:tracing:1.2.0" //Fix an issue with testing

    coreLibraryDesugaring "com.android.tools:desugar_jdk_libs:2.0.4"

    //Health & Connect
    implementation "androidx.health.connect:connect-client:1.0.0-alpha07"

    implementation 'nl.dionsegijn:konfetti-compose:2.0.4'

    implementation "androidx.concurrent:concurrent-futures-ktx:1.1.0"*/
}