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

package dev.rahmouni.neil.counters.feature.settings.main.model

import android.app.Activity
import android.content.Context
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.rahmouni.neil.counters.core.auth.AuthHelper
import dev.rahmouni.neil.counters.core.data.repository.userData.UserDataRepository
import dev.rahmouni.neil.counters.feature.settings.BuildConfig
import dev.rahmouni.neil.counters.feature.settings.main.model.SettingsUiState.Success
import dev.rahmouni.neil.counters.feature.settings.main.model.data.InAppUpdateState
import dev.rahmouni.neil.counters.feature.settings.main.model.data.InAppUpdateState.DownloadingUpdate
import dev.rahmouni.neil.counters.feature.settings.main.model.data.InAppUpdateState.NoUpdateAvailable
import dev.rahmouni.neil.counters.feature.settings.main.model.data.InAppUpdateState.UpdateAvailable
import dev.rahmouni.neil.counters.feature.settings.main.model.data.InAppUpdateState.WaitingForRestart
import dev.rahmouni.neil.counters.feature.settings.main.model.data.SettingsData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds

const val DEMO__IN_APP_UPDATE_AVAILABLE = true
const val DEMO__DELAY_BEFORE_IN_APP_UPDATE: Long = 500

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    authHelper: AuthHelper,
) : ViewModel() {

    private var devSettingsEnabled = MutableStateFlow(false)
    private var inAppUpdateState = MutableStateFlow<InAppUpdateState>(NoUpdateAvailable)

    val uiState: StateFlow<SettingsUiState> = combine(
        authHelper.getUserFlow(),
        userDataRepository.userData,
        devSettingsEnabled,
        inAppUpdateState,
    ) { user, userData, devSettingsEnabled, inAppUpdateData ->
        Success(
            SettingsData(
                user = user,
                devSettingsEnabled = devSettingsEnabled,
                inAppUpdateData = inAppUpdateData,
                hasTravelModeEnabled = userData.hasTravelModeEnabled,
                hasFriendsMainEnabled = userData.hasFriendsMainEnabled,
            ),
        )
    }.stateIn(
        scope = viewModelScope,
        initialValue = Success(
            SettingsData(
                user = authHelper.getUser(),
                devSettingsEnabled = devSettingsEnabled.value,
                inAppUpdateData = inAppUpdateState.value,
                hasTravelModeEnabled = false,
                hasFriendsMainEnabled = false,
            ),
        ),
        started = WhileSubscribed(5.seconds.inWholeMilliseconds),
    )

    fun checkForInAppUpdate(appUpdateManager: AppUpdateManager) {
        viewModelScope.launch {
            if (BuildConfig.FLAVOR.contentEquals("demo")) {
                if (DEMO__IN_APP_UPDATE_AVAILABLE) {
                    delay(DEMO__DELAY_BEFORE_IN_APP_UPDATE)
                    inAppUpdateState.compareAndSet(
                        inAppUpdateState.value,
                        UpdateAvailable(null, null),
                    )
                }
            } else {
                appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                        inAppUpdateState.compareAndSet(
                            inAppUpdateState.value,
                            UpdateAvailable(appUpdateManager, appUpdateInfo),
                        )
                    }
                }
            }
        }
    }

    fun performInAppUpdateAction(
        context: Context,
        launcher: ManagedActivityResultLauncher<IntentSenderRequest, ActivityResult>,
    ) {
        viewModelScope.launch {
            val demo = BuildConfig.FLAVOR.contentEquals("demo")
            when (inAppUpdateState.value) {
                is UpdateAvailable -> {
                    val appUpdateManager =
                        (inAppUpdateState.value as UpdateAvailable).appUpdateManager
                    val appUpdateInfo = (inAppUpdateState.value as UpdateAvailable).appUpdateInfo

                    if (!demo && appUpdateManager != null && appUpdateInfo != null) {
                        val listener = InstallStateUpdatedListener { state ->
                            inAppUpdateState.compareAndSet(
                                inAppUpdateState.value,
                                when (state.installStatus()) {
                                    InstallStatus.DOWNLOADING -> DownloadingUpdate(
                                        state.bytesDownloaded() / state.totalBytesToDownload()
                                            .toFloat(),
                                    )

                                    InstallStatus.DOWNLOADED -> WaitingForRestart(appUpdateManager)
                                    else -> NoUpdateAvailable
                                },
                            )
                        }

                        appUpdateManager.registerListener(listener)

                        appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            launcher,
                            AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build(),
                        )
                    } else if (demo) {
                        var progress = 0f

                        while (progress < 1f) {
                            inAppUpdateState.compareAndSet(
                                inAppUpdateState.value,
                                DownloadingUpdate(progress),
                            )

                            delay((250..750).random().toLong())
                            progress = min(progress + (1..33).random() / 100f, 1f)
                        }

                        inAppUpdateState.compareAndSet(
                            inAppUpdateState.value,
                            WaitingForRestart(null),
                        )
                    } else {
                        throw IllegalStateException("RahNeil_N3:IUi0odoQJdMeXIfwhCfuuHfFGlkhgVpv")
                    }
                }

                is WaitingForRestart -> {
                    val appUpdateManager =
                        (inAppUpdateState.value as WaitingForRestart).appUpdateManager

                    if (!demo && appUpdateManager != null) {
                        appUpdateManager.completeUpdate()
                    } else if (demo) {
                        with(context as Activity, function())
                    } else {
                        throw IllegalStateException("RahNeil_N3:Td2sNDcBF0ot05rVwQzzVs6qqd4QAcVv")
                    }
                }

                else ->
                    @Suppress("SpellCheckingInspection")
                    throw IllegalAccessException("RahNeil_N3:b0yHUf1FkIETpJ0EX3qpJuBNyvmeZarn")
            }
        }
    }

    private fun function(): Activity.() -> Unit = {
        finish()
        startActivity(intent)
    }

    fun setDevSettingsEnabled(enabled: Boolean) {
        devSettingsEnabled.compareAndSet(devSettingsEnabled.value, enabled)
    }

    fun setTravelMode(value: Boolean) {
        viewModelScope.launch {
            userDataRepository.setTravelMode(value)
        }
    }

    fun setFriendsMain(value: Boolean) {
        viewModelScope.launch {
            userDataRepository.setFriendsMain(value)
        }
    }

    fun logout() {
        viewModelScope.launch {
            userDataRepository.setShouldShowLoginScreenOnStartup(true)
        }
    }
}
