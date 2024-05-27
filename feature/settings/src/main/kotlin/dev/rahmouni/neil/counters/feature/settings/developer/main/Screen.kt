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

package dev.rahmouni.neil.counters.feature.settings.developer.main

import android.app.Activity
import android.text.format.DateUtils
import android.util.Log
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.outlined.CloudDone
import androidx.compose.material.icons.outlined.DataArray
import androidx.compose.material.icons.outlined.DeleteForever
import androidx.compose.material.icons.outlined.Link
import androidx.compose.material.icons.outlined.LocalFireDepartment
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dev.rahmouni.neil.counters.core.common.copyText
import dev.rahmouni.neil.counters.core.data.model.LinkRn3UrlData
import dev.rahmouni.neil.counters.core.designsystem.Rn3PreviewScreen
import dev.rahmouni.neil.counters.core.designsystem.Rn3Theme
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileClick
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileCopy
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileHorizontalDivider
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSmallHeader
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileSwitch
import dev.rahmouni.neil.counters.core.designsystem.icons.Rn3
import dev.rahmouni.neil.counters.feature.settings.BuildConfig
import dev.rahmouni.neil.counters.feature.settings.R
import dev.rahmouni.neil.counters.feature.settings.developer.links.navigateToDeveloperSettingsLinks

@Composable
internal fun DeveloperSettingsRoute(
    modifier: Modifier = Modifier,
    navController: NavController,
) {
    DeveloperSettingsScreen(
        modifier,
        onBackIconButtonClicked = navController::popBackStack,
        onLinksRn3UrlTileClicked = navController::navigateToDeveloperSettingsLinks,
    )
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun DeveloperSettingsScreen(
    modifier: Modifier = Modifier,
    onBackIconButtonClicked: () -> Unit = {},
    onLinksRn3UrlTileClicked: () -> Unit = {},
) {
    Rn3Scaffold(
        modifier,
        stringResource(R.string.feature_settings_developerSettingsScreen_topAppBarTitle),
        onBackIconButtonClicked,
    ) {
        DeveloperSettingsPanel(
            it,
            onLinksRn3UrlTileClicked,
        )
    }
}

@Composable
private fun DeveloperSettingsPanel(
    contentPadding: PaddingValues,
    onLinksRn3UrlTileClicked: () -> Unit,
) {
    val context = LocalContext.current

    LazyColumn(contentPadding = contentPadding) {
        // buildconfigTile
        item {
            Rn3TileClick(
                title = stringResource(R.string.feature_settings_developerSettingsScreen_buildconfigTile_title),
                icon = Icons.Outlined.Rn3,
                supportingText = BuildConfig.FLAVOR + " / " + BuildConfig.BUILD_TYPE,
            ) {}
        }

        // clearPersistenceTile
        item {
            @Suppress("KotlinConstantConditions")
            Rn3TileClick(
                title = stringResource(R.string.feature_settings_developerSettingsScreen_clearPersistenceTile_title),
                icon = Icons.Outlined.DeleteForever,
                enabled = BuildConfig.FLAVOR == "demo",
                supportingText = if (BuildConfig.FLAVOR != "demo") stringResource(R.string.feature_settings_developerSettingsScreen_clearPersistenceTile_supportingText) else null,
            ) {
                try {
                    Firebase.firestore.terminate().addOnCompleteListener {
                        Firebase.firestore.clearPersistence().addOnSuccessListener {
                            Toast.makeText(
                                context,
                                context.getString(R.string.feature_settings_developerSettingsScreen_clearPersistenceTile_success),
                                Toast.LENGTH_SHORT,
                            ).show()
                            (context as Activity).recreate()
                        }
                    }
                } catch (e: Exception) {
                    context.copyText("Error", "$e | ${e.message}")
                }
            }
        }

        // linksRn3UrlTile
        item {
            Rn3TileClick(
                title = stringResource(R.string.feature_settings_developerSettingsScreen_linksRn3UrlTile_title),
                icon = Icons.Outlined.Link,
                onClick = onLinksRn3UrlTileClicked,
            )
        }

        FirebaseApp.getApps(context)
            .map {
                Triple(
                    it.name,
                    FirebaseRemoteConfig.getInstance(it),
                    FirebaseInstallations.getInstance(it).id,
                )
            }
            .forEach { (appName, remoteConfig, installation) ->

                item { Rn3TileHorizontalDivider() }

                item {
                    Rn3TileSmallHeader(
                        title = stringResource(
                            R.string.feature_settings_developerSettingsScreen_configHeaderTile_title,
                            appName,
                        ),
                    )
                }

                item {
                    Rn3TileCopy(
                        title = stringResource(R.string.feature_settings_developerSettingsScreen_firebaseIdTile_title),
                        icon = Icons.Outlined.LocalFireDepartment,
                        text = installation.result,
                    )
                }

                item {
                    Rn3TileClick(
                        title = stringResource(R.string.feature_settings_developerSettingsScreen_rcStatusTile_title),
                        icon = Icons.Outlined.DataArray,
                        supportingText = when (remoteConfig.info.lastFetchStatus) {
                            FirebaseRemoteConfig.LAST_FETCH_STATUS_SUCCESS ->
                                stringResource(
                                    R.string.feature_settings_developerSettingsScreen_rcStatusTile_success,
                                    DateUtils.getRelativeTimeSpanString(
                                        remoteConfig.info.fetchTimeMillis,
                                    ),
                                )

                            FirebaseRemoteConfig.LAST_FETCH_STATUS_FAILURE -> stringResource(R.string.feature_settings_developerSettingsScreen_rcStatusTile_failure)
                            FirebaseRemoteConfig.LAST_FETCH_STATUS_THROTTLED -> stringResource(R.string.feature_settings_developerSettingsScreen_rcStatusTile_throttled)
                            FirebaseRemoteConfig.LAST_FETCH_STATUS_NO_FETCH_YET -> stringResource(R.string.feature_settings_developerSettingsScreen_rcStatusTile_noFetchYet)
                            else -> "RahNeil_N3:Error:NMdUsSOSmdgHuvcFuFr6WjorE25ZszWZ"
                        },
                    ) {}
                }

                remoteConfig.all.entries.forEach { (key, value) ->
                    val icon = when (value.source) {
                        FirebaseRemoteConfig.VALUE_SOURCE_REMOTE -> Icons.Outlined.CloudDone
                        FirebaseRemoteConfig.VALUE_SOURCE_DEFAULT -> Icons.AutoMirrored.Outlined.InsertDriveFile
                        else -> Icons.Outlined.QuestionMark
                    }

                    if (value.asString().isBoolean()) {
                        item {
                            Rn3TileSwitch(title = key, icon = icon, checked = value.asBoolean()) {}
                        }
                    } else {
                        item {
                            Rn3TileCopy(title = key, icon = icon, text = value.asString())
                        }
                    }
                }
            }
    }
}

private fun String.isBoolean(): Boolean = this == "true" || this == "false"

@Rn3PreviewScreen
@Composable
private fun Default() {
    Rn3Theme {
        DeveloperSettingsScreen()
    }
}
