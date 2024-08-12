/*
 * Copyright (C) 2024 Rahmouni Neïl & Aurélien Coppée
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

@file:Suppress("UnusedReceiverParameter")

package dev.rahmouni.neil.counters.core.designsystem.icons

import androidx.compose.material.icons.Icons.Outlined
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import dev.rahmouni.neil.counters.core.designsystem.R

val customIconsList
    @Composable
    get() = listOf(
        Outlined.Tooltip,
        Outlined.Logo,
        Outlined.Contract,
        Outlined.Discord,
        Outlined.Instagram,
        Outlined.Mastodon,
        Outlined.Linkedin,
        Outlined.Gitlab,
        Outlined.Threads,
        Outlined.SyncSavedLocally,
        Outlined.DevicesOff,

        Outlined.Building,
        Outlined.Road,
        Outlined.Bank,
        Outlined.HomeGroup,
        Outlined.HumanGreetingProximity,
        Outlined.Belgium,
        Outlined.France,
        Outlined.UK,
        Outlined.USA,
    )

val Outlined.Tooltip
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_tooltip)

val Outlined.Logo
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_logo)

val Outlined.Contract
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_contract)

val Outlined.Discord
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_discord)

val Outlined.Instagram
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_instagram)

val Outlined.Mastodon
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_mastodon)

val Outlined.Linkedin
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_linkedin)

val Outlined.Gitlab
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_gitlab)

val Outlined.Threads
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_threads)

val Outlined.SyncSavedLocally
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_syncsavedlocally)

val Outlined.DevicesOff
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_devicesoff)

val Outlined.Building
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_building)

val Outlined.Road
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_road)

val Outlined.Bank
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_bank)

val Outlined.HomeGroup
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_home_group)

val Outlined.HumanGreetingProximity
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_human_greeting_proximity)

val Outlined.Belgium
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_flag_be)

val Outlined.France
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_flag_fr)

val Outlined.UK
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_flag_uk)

val Outlined.USA
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_flag_us)

// Animated icons

val customAnimatedIconsList
    @Composable
    get() = listOf(
        Outlined.CoffeeAnimated,
        Outlined.OpenInNewAnimated,
    )

val Outlined.CoffeeAnimated: AnimatedIcon
    get() = AnimatedIcon(R.drawable.core_designsystem_icon_coffee_animated, 1500)

val Outlined.OpenInNewAnimated: AnimatedIcon
    get() = AnimatedIcon(R.drawable.core_designsystem_icon_openinnew_animated, 500)
