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
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber
import dev.rahmouni.neil.counters.core.common.Rn3Uri
import dev.rahmouni.neil.counters.core.common.toRn3Uri
import dev.rahmouni.neil.counters.core.config.LocalConfigHelper
import dev.rahmouni.neil.counters.core.designsystem.TopAppBarAction
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3ExpandableSurface
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3LargeButton
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3OutlinedTextField
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Scaffold
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3TextDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.TopAppBarStyle.HOME
import dev.rahmouni.neil.counters.core.designsystem.component.tile.Rn3TileUri
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3AdditionalBigPadding
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValues
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.HORIZONTAL
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.rebased.Rn3PhoneForm
import dev.rahmouni.neil.counters.core.designsystem.rebased.icon
import dev.rahmouni.neil.counters.core.designsystem.rebased.sortedCountries
import dev.rahmouni.neil.counters.core.designsystem.rebased.text
import dev.rahmouni.neil.counters.core.feedback.FeedbackContext.FeedbackScreenContext
import dev.rahmouni.neil.counters.core.feedback.navigateToFeedback
import dev.rahmouni.neil.counters.core.model.data.AddressInfo
import dev.rahmouni.neil.counters.core.model.data.Country
import dev.rahmouni.neil.counters.core.ui.TrackScreenViewEvent
import dev.rahmouni.neil.counters.feature.information.model.InformationUiState.Loading
import dev.rahmouni.neil.counters.feature.information.model.InformationUiState.Success
import dev.rahmouni.neil.counters.feature.information.model.InformationViewModel
import dev.rahmouni.neil.counters.feature.information.model.data.InformationData
import kotlinx.coroutines.delay


@Composable
internal fun InformationRoute(
    modifier: Modifier = Modifier,
    viewModel: InformationViewModel = hiltViewModel(),
    navController: NavController,
    navigateToNextPage: () -> Unit,
) {
    val config = LocalConfigHelper.current

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        Loading -> {}
        is Success -> InformationScreen(
            modifier = modifier,
            data = (uiState as Success).informationData,
        feedbackTopAppBarAction = FeedbackScreenContext(
            "ConnectScreen",
            "oujWHHHpuFbChUEYhyGX39V2exJ299Dw",
        ).toTopAppBarAction(navController::navigateToFeedback),
            onValidateLocationClicked = { address, phone ->
                viewModel.save(address, phone)
                navigateToNextPage()
            },
        privacyPolicyTileUri = config.getString("privacy_policy_url").toRn3Uri {
        },
        )
    }

    TrackScreenViewEvent(screenName = "information")
}

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
@Composable
internal fun InformationScreen(
    modifier: Modifier = Modifier,
    data: InformationData,
    feedbackTopAppBarAction: TopAppBarAction? = null,
    onValidateLocationClicked: (AddressInfo, PhoneNumber?) -> Unit,
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
        InformationForm(it, data, onValidateLocationClicked, privacyPolicyTileUri)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun InformationForm(
    paddingValues: Rn3PaddingValues,
    data: InformationData,
    onValidateLocationClicked: (AddressInfo, PhoneNumber?) -> Unit,
    privacyPolicyTileUri: Rn3Uri,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState()),
    ) {
        var address by remember { mutableStateOf(value = data.address) }
        var phone by remember { mutableStateOf(value = data.phone) }

        var hasUserInteracted by rememberSaveable { mutableStateOf(value = false) }

        var countryExpanded by remember { mutableStateOf(value = false) }

        var isFocusedPostalCode by rememberSaveable { mutableStateOf(value = false) }
        var showFullLabelPostalCode by rememberSaveable { mutableStateOf(value = false) }

        LaunchedEffect(isFocusedPostalCode) {
            if (isFocusedPostalCode) {
                delay(timeMillis = 50)
                showFullLabelPostalCode = true
            } else {
                showFullLabelPostalCode = false
            }
        }

        Rn3ExpandableSurface(
            content = {
                Icon(imageVector = Outlined.Info, contentDescription = null)
                Text(
                    text = "Why do we need this",
                    modifier = Modifier.padding(Rn3TextDefaults.paddingValues.only(HORIZONTAL)
                    )
                )
            },
            expandedContent = {
                Column {
                    Text(
                        modifier = Modifier.padding(
                            Rn3TextDefaults.paddingValues.copy(
                                top = 0.dp,
                            ),
                        ),
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

        Column(modifier = Modifier.padding(Rn3AdditionalBigPadding.paddingValues.only(HORIZONTAL))) {

        ExposedDropdownMenuBox(
            expanded = countryExpanded,
            onExpandedChange = { countryExpanded = !countryExpanded },
        ) {
            Rn3OutlinedTextField(
                readOnly = true,
                value = address.country?.text() ?: "",
                onValueChange = {},
                label = { Text(text = "Country") },
                hasUserInteracted = hasUserInteracted,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryExpanded)
                },
                colors = OutlinedTextFieldDefaults.colors(),
                modifier = Modifier
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                    .fillMaxWidth(),
                enableAutofill = true,
                autofillTypes = AutofillType.AddressCountry,
            )

            ExposedDropdownMenu(
                expanded = countryExpanded,
                onDismissRequest = { countryExpanded = false },
            ) {
                Country.sortedCountries().forEach { selectedCountry ->
                    DropdownMenuItem(
                        text = {
                            Row(verticalAlignment = CenterVertically) {
                                Icon(
                                    imageVector = selectedCountry.icon(),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Unspecified,
                                )
                                Spacer(modifier = Modifier.size(8.dp))
                                Text(text = selectedCountry.text())
                            }
                        },
                        onClick = {
                            countryExpanded = false
                            address = address.copy(country = selectedCountry)
                        },
                    )
                }
            }
        }

            Rn3OutlinedTextField(
                value = address.region ?: "",
                onValueChange = { newRegion -> address = address.copy(region = newRegion) },
                hasUserInteracted = hasUserInteracted,
                maxCharacters = 100,
                label = { Text(text = "Region") },
                beEmpty = true,
                singleLine = true,
                enableAutofill = true,
                autofillTypes = AutofillType.AddressRegion,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Rn3OutlinedTextField(
                modifier = Modifier.weight(1.5f),
                value = address.locality,
                onValueChange = { newLocality -> address = address.copy(locality = newLocality) },
                hasUserInteracted = hasUserInteracted,
            maxCharacters = 100,
                label = { Text(text = "Locality") },
            singleLine = true,
                enableAutofill = true,
                autofillTypes = AutofillType.AddressLocality,
        )

            Rn3OutlinedTextField(
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged { focusState ->
                        isFocusedPostalCode = focusState.isFocused
                    },
                value = address.postalCode ?: "",
                onValueChange = { newPostalCode ->
                    address = address.copy(postalCode = newPostalCode)
                },
                maxCharacters = 20,
                label = { Text(text = if (showFullLabelPostalCode || address.postalCode != "") "Postal Code" else "Postal") },
                beEmpty = true,
                singleLine = true,
                enableAutofill = true,
                autofillTypes = AutofillType.PostalCode,
            )
            }

        Rn3OutlinedTextField(
            value = address.street,
            onValueChange = { newStreet -> address = address.copy(street = newStreet) },
            hasUserInteracted = hasUserInteracted,
            maxCharacters = 200,
            label = { Text(text = "Street") },
            singleLine = false,
            enableAutofill = true,
            autofillTypes = AutofillType.AddressStreet,
        )

        Rn3OutlinedTextField(
            value = address.auxiliaryDetails ?: "",
            onValueChange = { newAuxiliaryDetails ->
                address = address.copy(auxiliaryDetails = newAuxiliaryDetails)
            },
            maxCharacters = 200,
            label = { Text(text = "Auxiliary details") },
            hasUserInteracted = hasUserInteracted,
            beEmpty = true,
            singleLine = false,
            enableAutofill = true,
            autofillTypes = AutofillType.AddressAuxiliaryDetails,
        )

            Rn3PhoneForm(
                phone = phone ?: PhoneNumber(),
                onPhoneChanged = { updatedPhoneNumber -> phone = updatedPhoneNumber },
                hasUserInteracted = hasUserInteracted,
                beEmpty = true,
            )

            Rn3LargeButton(
                text = "Save",
                icon = Outlined.Check,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                hasUserInteracted = true
                if (address.country != null && address.street.isNotEmpty() && address.locality.isNotEmpty()) {
                    onValidateLocationClicked(address, phone)
                }
            }
    }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun Form(
    paddingValues: Rn3PaddingValues,
    data: InformationData,
    onValidateLocationClicked: (AddressInfo, PhoneNumber?) -> Unit,
    privacyPolicyTileUri: Rn3Uri,
) {
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .verticalScroll(rememberScrollState()),
    ) {
        var address by remember { mutableStateOf(value = data.address) }
        var phone by remember { mutableStateOf(value = data.phone) }

        var hasUserInteracted by rememberSaveable { mutableStateOf(value = false) }

        var countryExpanded by remember { mutableStateOf(value = false) }

        var isFocusedPostalCode by rememberSaveable { mutableStateOf(value = false) }
        var showFullLabelPostalCode by rememberSaveable { mutableStateOf(value = false) }

        LaunchedEffect(isFocusedPostalCode) {
            if (isFocusedPostalCode) {
                delay(timeMillis = 50)
                showFullLabelPostalCode = true
            } else {
                showFullLabelPostalCode = false
            }
        }

        Rn3ExpandableSurface(
            content = {
                Icon(imageVector = Outlined.Info, contentDescription = null)
                Text(
                    text = "Why do we need this",
                    modifier = Modifier.padding(Rn3TextDefaults.paddingValues.only(HORIZONTAL)
                    )
                )
            },
            expandedContent = {
                Column {
                    Text(
                        modifier = Modifier.padding(
                            Rn3TextDefaults.paddingValues.copy(
                                top = 0.dp,
                            ),
                        ),
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

        Column(modifier = Modifier.padding(Rn3AdditionalBigPadding.paddingValues.only(HORIZONTAL))) {

            ExposedDropdownMenuBox(
                expanded = countryExpanded,
                onExpandedChange = { countryExpanded = !countryExpanded },
            ) {
                Rn3OutlinedTextField(
                    readOnly = true,
                    value = address.country?.text() ?: "",
                    onValueChange = {},
                    label = { Text(text = "Country") },
                    hasUserInteracted = hasUserInteracted,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryExpanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(),
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                        .fillMaxWidth(),
                    enableAutofill = true,
                    autofillTypes = AutofillType.AddressCountry,
                )

                ExposedDropdownMenu(
                    expanded = countryExpanded,
                    onDismissRequest = { countryExpanded = false },
                ) {
                    Country.sortedCountries().forEach { selectedCountry ->
                        DropdownMenuItem(
                            text = {
                                Row(verticalAlignment = CenterVertically) {
                                    Icon(
                                        imageVector = selectedCountry.icon(),
                                        contentDescription = null,
                                        modifier = Modifier.size(24.dp),
                                        tint = Color.Unspecified,
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(text = selectedCountry.text())
                                }
                            },
                            onClick = {
                                countryExpanded = false
                                address = address.copy(country = selectedCountry)
                            },
                        )
                    }
                }
            }

            Rn3OutlinedTextField(
                value = address.region ?: "",
                onValueChange = { newRegion -> address = address.copy(region = newRegion) },
                hasUserInteracted = hasUserInteracted,
                maxCharacters = 100,
                label = { Text(text = "Region") },
                beEmpty = true,
                singleLine = true,
                enableAutofill = true,
                autofillTypes = AutofillType.AddressRegion,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Rn3OutlinedTextField(
                    modifier = Modifier.weight(1.5f),
                    value = address.locality,
                    onValueChange = { newLocality -> address = address.copy(locality = newLocality) },
                    hasUserInteracted = hasUserInteracted,
                    maxCharacters = 100,
                    label = { Text(text = "Locality") },
                    singleLine = true,
                    enableAutofill = true,
                    autofillTypes = AutofillType.AddressLocality,
                )

                Rn3OutlinedTextField(
                    modifier = Modifier
                        .weight(1f)
                        .onFocusChanged { focusState ->
                            isFocusedPostalCode = focusState.isFocused
                        },
                    value = address.postalCode ?: "",
                    onValueChange = { newPostalCode ->
                        address = address.copy(postalCode = newPostalCode)
                    },
                    maxCharacters = 20,
                    label = { Text(text = if (showFullLabelPostalCode || address.postalCode != "") "Postal Code" else "Postal") },
                    beEmpty = true,
                    singleLine = true,
                    enableAutofill = true,
                    autofillTypes = AutofillType.PostalCode,
                )
            }

            Rn3OutlinedTextField(
                value = address.street,
                onValueChange = { newStreet -> address = address.copy(street = newStreet) },
                hasUserInteracted = hasUserInteracted,
                maxCharacters = 200,
                label = { Text(text = "Street") },
                singleLine = false,
                enableAutofill = true,
                autofillTypes = AutofillType.AddressStreet,
            )

            Rn3OutlinedTextField(
                value = address.auxiliaryDetails ?: "",
                onValueChange = { newAuxiliaryDetails ->
                    address = address.copy(auxiliaryDetails = newAuxiliaryDetails)
                },
                maxCharacters = 200,
                label = { Text(text = "Auxiliary details") },
                hasUserInteracted = hasUserInteracted,
                beEmpty = true,
                singleLine = false,
                enableAutofill = true,
                autofillTypes = AutofillType.AddressAuxiliaryDetails,
            )

            Rn3PhoneForm(
                phone = phone ?: PhoneNumber(),
                onPhoneChanged = { updatedPhoneNumber -> phone = updatedPhoneNumber },
                hasUserInteracted = hasUserInteracted,
                beEmpty = true,
            )

            Rn3LargeButton(
                text = "Save",
                icon = Outlined.Check,
                color = MaterialTheme.colorScheme.primaryContainer,
            ) {
                hasUserInteracted = true
                if (address.country != null && address.street.isNotEmpty() && address.locality.isNotEmpty()) {
                    onValidateLocationClicked(address, phone)
                }
            }
        }
    }
}
