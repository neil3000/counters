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
        Outlined.Rn3,
        Outlined.Contract,
        Outlined.Discord,
        Outlined.Instagram,
        Outlined.Mastodon,
        Outlined.Linkedin,
        Outlined.Gitlab,
        Outlined.Threads,
        Outlined.SyncSavedLocally,
        Outlined.DevicesOff,
    )

val Outlined.Tooltip
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_tooltip)

val Outlined.Rn3
    @Composable
    get() = ImageVector.vectorResource(id = R.drawable.core_designsystem_icon_rn3)

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

// Animated icons

val customAnimatedIconsList
    @Composable
    get() = listOf(Outlined.CoffeeAnimated, Outlined.OpenInNewAnimated)

val Outlined.CoffeeAnimated: AnimatedIcon
    get() = AnimatedIcon(R.drawable.core_designsystem_icon_coffee_animated, 1500)

val Outlined.OpenInNewAnimated: AnimatedIcon
    get() = AnimatedIcon(R.drawable.core_designsystem_icon_openinnew_animated, 500)
