/*
 * Copyright 2024 Rahmouni Neïl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.rahmouni.neil.counters

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.metrics.performance.JankStats
import com.google.firebase.Firebase
import com.google.firebase.analytics.analytics
import com.google.firebase.crashlytics.crashlytics
import com.google.firebase.firestore.firestore
import dagger.hilt.android.AndroidEntryPoint
import dev.rahmouni.neil.counters.MainActivityUiState.Loading
import dev.rahmouni.neil.counters.MainActivityUiState.Success
import dev.rahmouni.neil.counters.core.accessibility.LocalAccessibilityHelper
import dev.rahmouni.neil.counters.core.analytics.AnalyticsHelper
import dev.rahmouni.neil.counters.core.analytics.LocalAnalyticsHelper
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.auth.LocalAuthHelper
import dev.rahmouni.neil.counters.core.config.ConfigHelper
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.user.Rn3User.LoggedOutUser
import dev.rahmouni.neil.counters.ui.CountersApp
import dev.rahmouni.neil.counters.ui.rememberCountersAppState
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import rahmouni.neil.counters.BuildConfig
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * Lazily inject [JankStats], which is used to track jank throughout the app.
     */
    @Inject
    lateinit var lazyStats: dagger.Lazy<JankStats>

    @Inject
    lateinit var analyticsHelper: AnalyticsHelper

    @Inject
    lateinit var configHelper: ConfigHelper

    @Inject
    lateinit var authHelper: AuthHelper

    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        var uiState: MainActivityUiState by mutableStateOf(Loading)

        // Enable offline persistence for Firestore
        Firebase.firestore.persistentCacheIndexManager?.apply {
            enableIndexAutoCreation()
        }

        // Update the uiState
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState
                    .onEach {
                        uiState = it

                        if (it is Success) {
                            Firebase.analytics.setAnalyticsCollectionEnabled(!BuildConfig.DEBUG && it.hasMetricsEnabled)
                            Firebase.crashlytics.setCrashlyticsCollectionEnabled(!BuildConfig.DEBUG && it.hasCrashlyticsEnabled)

                            if (it.isAppFirstLaunch) {
                                // If the user is not signed in, launch the sign in process.
                                if (authHelper.getUser() is LoggedOutUser) {
                                    authHelper.quickFirstSignIn(this@MainActivity)
                                }

                                viewModel.setNotAppFirstLaunch()
                            }
                        }

                        @Suppress("KotlinConstantConditions")
                        if (BuildConfig.FLAVOR == "demo") {
                            Firebase.firestore.disableNetwork()
                        } else {
                            Firebase.firestore.enableNetwork()
                        }
                    }.collect()
            }
        }

        // Keep the splash screen on-screen until the UI state is loaded. This condition is
        // evaluated each time the app needs to be redrawn so it should be fast to avoid blocking
        // the UI.
        splashScreen.setKeepOnScreenCondition { uiState is Loading }

        configHelper.init(this)

        setContent {
            val appState = rememberCountersAppState()

            enableEdgeToEdge()

            if (uiState is Success) {
                CompositionLocalProvider(
                    LocalAnalyticsHelper provides analyticsHelper,
                    LocalAccessibilityHelper provides (uiState as Success).accessibilityHelper,
                    LocalConfigHelper provides configHelper,
                    LocalAuthHelper provides authHelper,
                ) {
                    Rn3Theme {
                        CountersApp(
                            appState,
                            showLoginScreen = (uiState as Success).shouldShowLoginScreenOnStartup,
                        )
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        lazyStats.get().isTrackingEnabled = true
    }

    override fun onPause() {
        super.onPause()
        lazyStats.get().isTrackingEnabled = false
    }
}
