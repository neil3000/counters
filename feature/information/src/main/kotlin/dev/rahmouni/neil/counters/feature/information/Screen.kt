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

package dev.rahmouni.neil.counters.feature.information

import androidx.annotation.VisibleForTesting
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.AutofillType
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import dev.rahmouni.neil.counters.core.common.Rn3Uri
import dev.rahmouni.neil.counters.core.common.toRn3Uri
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurface
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LargeButton
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3OutlinedTextField
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.HOME
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileUri
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.rebased.Country
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.feature.information.model.InformationViewModel


@Composable
internal fun InformationRoute(
    modifier: Modifier = Modifier,
    viewModel: InformationViewModel = hiltViewModel(),
    navController: NavController,
    navigateToNextPage: () -> Unit,
) {
    val config = LocalConfigHelper.current

    ConnectScreen(
        modifier,
        feedbackTopAppBarAction = FeedbackScreenContext(
            "ConnectScreen",
            "oujWHHHpuFbChUEYhyGX39V2exJ299Dw",
        ).toTopAppBarAction(navController::navigateToFeedback),
        onValidateLocationClicked = {
            viewModel.save()
            navigateToNextPage()
        },
        privacyPolicyTileUri = config.getString("privacy_policy_url").toRn3Uri {
        },
    )

    TrackScreenViewEvent(screenName = "information")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun ConnectScreen(
    modifier: Modifier = Modifier,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onValidateLocationClicked: () -> Unit = {},
    privacyPolicyTileUri: Rn3Uri = Rn3Uri.AndroidPreview,
) {
    Rn3Scaffold(
        modifier = modifier,
        topAppBarTitle = "",
        topAppBarTitleAlignment = CenterHorizontally,
        onBackIconButtonClicked = null,
        topAppBarActions = listOfNotNull(feedbackTopAppBarAction),
        topAppBarStyle = HOME,
    ) {
        ColumnPanel(it, onValidateLocationClicked, privacyPolicyTileUri)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun ColumnPanel(
    paddingValues: Rn3PaddingValues,
    onValidateLocationClicked: () -> Unit = {},
    privacyPolicyTileUri: Rn3Uri,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState()),
    ) {
        var country by rememberSaveable { mutableStateOf<Country?>(null) }
        var locality by rememberSaveable { mutableStateOf("") }
        var street by rememberSaveable { mutableStateOf("") }
        var postalCode by rememberSaveable { mutableStateOf("") }
        var region by rememberSaveable { mutableStateOf("") }
        var auxiliaryDetails by rememberSaveable { mutableStateOf("") }
        var phoneNumber by rememberSaveable { mutableStateOf("") }
        var phoneCountryCode by rememberSaveable { mutableStateOf<Country?>(null) }

        var isEmpty by rememberSaveable { mutableStateOf(true) }
        var hasUserInteracted by rememberSaveable { mutableStateOf(false) }

        var expanded by remember { mutableStateOf(false) }

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            Rn3OutlinedTextField(
                readOnly = true,
                value = country?.text ?: "",
                onValueChange = {},
                label = { Text(text = "Country") },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                    .fillMaxWidth(),
                onEmptyStateChange = { emptyState -> isEmpty = emptyState },
                enableAutofill = true,
                autofillTypes = AutofillType.AddressCountry,
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                Country.entries.sortedBy { it.text }.forEach { selectedCountry ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = CenterVertically) {
                                Icon(
                                    imageVector = Country.getIcon(selectedCountry),
                                    contentDescription = "Flag of ${selectedCountry.text}",
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Unspecified,
                                )
                                Spacer(Modifier.size(16.dp))
                                Text(text = selectedCountry.text)
                            }
                        },
                        onClick = {
                            expanded = false
                            country = selectedCountry
                            isEmpty = false
                        },
                    )
                }
            }
        }

            Rn3OutlinedTextField(
                value = region,
                onValueChange = { newText -> region = newText; hasUserInteracted = true },
                onEmptyStateChange = { emptyState -> isEmpty = emptyState },
                hasUserInteracted = hasUserInteracted,
                maxCharacters = 100,
                label = { Text(text = "Region") },
                beEmpty = true,
                singleLine = true,
                enableAutofill = true,
                autofillTypes = AutofillType.AddressRegion,
            )

            Rn3OutlinedTextField(
                value = locality,
                onValueChange = { newText -> locality = newText; hasUserInteracted = true },
            onEmptyStateChange = { emptyState -> isEmpty = emptyState },
                hasUserInteracted = hasUserInteracted,
            maxCharacters = 100,
                label = { Text(text = "Locality") },
            singleLine = true,
                enableAutofill = true,
                autofillTypes = AutofillType.AddressLocality,
        )

            Rn3OutlinedTextField(
                value = postalCode,
                onValueChange = { newText -> postalCode = newText; hasUserInteracted = true },
                onEmptyStateChange = { emptyState -> isEmpty = emptyState },
                hasUserInteracted = hasUserInteracted,
                maxCharacters = 20,
                label = { Text(text = "Postal Code") },
                beEmpty = true,
                singleLine = true,
                enableAutofill = true,
                autofillTypes = AutofillType.PostalCode,
            )

        Rn3OutlinedTextField(
            value = street,
            onValueChange = { newText -> street = newText; hasUserInteracted = true },
            onEmptyStateChange = { emptyState -> isEmpty = emptyState },
            hasUserInteracted = hasUserInteracted,
            maxCharacters = 200,
            label = { Text(text = "Street") },
            singleLine = false,
            enableAutofill = true,
            autofillTypes = AutofillType.AddressStreet,
        )

        Rn3OutlinedTextField(
            value = auxiliaryDetails,
            onValueChange = { newText -> auxiliaryDetails = newText; hasUserInteracted = true },
            onEmptyStateChange = { emptyState -> isEmpty = emptyState },
            hasUserInteracted = hasUserInteracted,
            maxCharacters = 200,
            label = { Text(text = "Auxiliary details") },
            beEmpty = true,
            singleLine = false,
            enableAutofill = true,
            autofillTypes = AutofillType.AddressAuxiliaryDetails,
        )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                ExposedDropdownMenuBox(
                    modifier = Modifier.weight(1f),
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                ) {
                    Rn3OutlinedTextField(
                        readOnly = true,
                        value = phoneCountryCode?.text ?: "",
                        onValueChange = {},
                        label = { Text(text = "") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                        },
                        colors = OutlinedTextFieldDefaults.colors(),
                        modifier = Modifier
                            .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                            .fillMaxWidth(),
                        onEmptyStateChange = { emptyState -> isEmpty = emptyState },
                        enableAutofill = true,
                        autofillTypes = AutofillType.PhoneCountryCode,
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        Country.entries.sortedBy { it.text }.forEach { selectedCountry ->
                            DropdownMenuItem(
                                text = {
                                    Row(verticalAlignment = CenterVertically) {
                                        Icon(
                                            imageVector = Country.getIcon(selectedCountry),
                                            contentDescription = "Flag of ${selectedCountry.text}",
                                            modifier = Modifier.size(24.dp),
                                            tint = Color.Unspecified,
                                        )
                                        Spacer(Modifier.size(16.dp))
                                        Text(text = ("+" + selectedCountry.phoneCode.toString()))
                                    }
                                },
                                onClick = {
                                    expanded = false
                                    phoneCountryCode = selectedCountry
                                    isEmpty = false
                                },
                            )
                        }
                    }
                }

                Rn3OutlinedTextField(
                    modifier = Modifier.weight(2f),
                    value = phoneNumber,
                    onValueChange = { newText -> phoneNumber = newText; hasUserInteracted = true },
                    onEmptyStateChange = { emptyState -> isEmpty = emptyState },
                    hasUserInteracted = hasUserInteracted,
                    maxCharacters = 20,
                    label = { Text(text = "Phone number") },
                    beEmpty = true,
                    singleLine = true,
                    enableAutofill = true,
                    autofillTypes = AutofillType.PhoneNumber,
                )
            }
        }

     Rn3LargeButton(
         modifier = Modifier.padding(horizontal = 32.dp, vertical = 16.dp),
         text = "Save",
         icon = Outlined.Check,
         color = MaterialTheme.colorScheme.primary,
     ) {
         hasUserInteracted = true
         if (!isEmpty) onValidateLocationClicked()
     }

        Rn3ExpandableSurface(
            content = {
                Icon(imageVector = Outlined.Info, contentDescription = null)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Why do we need this")
            },
            expandedContent = {
                Column {
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = "The core functionality of our application is based on your location",
                    )
                    Rn3TileUri(
                        title = "Privacy policy",
                        icon = Outlined.Policy,
                        uri = privacyPolicyTileUri,
                    )
                }
            },
        )
    }
}
