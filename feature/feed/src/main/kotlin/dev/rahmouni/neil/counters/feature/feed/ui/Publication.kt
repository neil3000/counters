package dev.rahmouni.neil.counters.feature.feed.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Report
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.data.model.FriendEntity
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3SurfaceDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3Switch
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3TextDefaults
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.HORIZONTAL
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.Rn3PaddingValuesDirection.TOP
import dev.rahmouni.neil.counters.core.designsystem.paddingValues.padding
import dev.rahmouni.neil.counters.core.designsystem.rebased.PostType
import dev.rahmouni.neil.counters.core.designsystem.rebased.SharingScope
import dev.rahmouni.neil.counters.core.designsystem.rebased.text
import dev.rahmouni.neil.counters.core.model.data.Country
import dev.rahmouni.neil.counters.feature.feed.R.string
import dev.rahmouni.neil.counters.feature.feed.model.PostEntity
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

@Composable
fun Publication(post: PostEntity, enabled: Boolean, friend: FriendEntity? = null) {
    val haptic = getHaptic()
    val context = LocalContext.current

    var expanded by remember { mutableStateOf(value = false) }
    val indication = LocalIndication.current
    var cat1Checked = true
    var cat2Checked = true

    val cat1InteractionSource = remember { MutableInteractionSource() }
    val cat2InteractionSource = remember { MutableInteractionSource() }

    @Composable
    fun timeElapsed(timestamp: LocalDateTime): String {
        val now = LocalDateTime.now()
        val totalMinutes = ChronoUnit.MINUTES.between(timestamp, now)

        return when {
            totalMinutes >= (24 * 60) -> "${totalMinutes / (24 * 60)}" + stringResource(string.feature_feed_timeElapsed_day)
            totalMinutes >= 60 -> "${totalMinutes / 60}" + stringResource(string.feature_feed_timeElapsed_hours)
            totalMinutes > 0 -> "$totalMinutes" + stringResource(string.feature_feed_timeElapsed_minute)
            else -> stringResource(string.feature_feed_timeElapsed_now)
        }
    }

    Column {
        Surface(tonalElevation = Rn3SurfaceDefaults.tonalElevation) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Rn3SurfaceDefaults.paddingValues),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (friend != null) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(6.dp),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        )
                    } else {
                        post.sharingScope.DisplayIcon(post.location)
                    }
                    Column(
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.padding(Rn3TextDefaults.paddingValues.only(HORIZONTAL)),
                    ) {
                        if (friend != null) {
                            Text(
                                text = friend.display(),
                                style = MaterialTheme.typography.titleMedium,
                            )
                        } else {
                            if (post.sharingScope == SharingScope.COUNTRY) {
                                Text(
                                    text = Country.getCountryFromIso(post.location)?.text()
                                        ?: "Country not found",
                                    fontWeight = FontWeight.Bold,
                                )
                            } else {
                                Text(
                                    text = post.location ?: "Location not found",
                                    style = MaterialTheme.typography.titleMedium,
                                )
                            }
                        }
                        Text(
                            text = timeElapsed(post.timestamp),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.alpha(0.5f),
                        )
                    }
                }
                Box {
                    Rn3IconButton(
                        icon = Icons.Default.MoreHoriz,
                        contentDescription = "Action on the publication",
                    ) { expanded = true }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("report publication") },
                            onClick = {
                                haptic.click()
                            },
                            leadingIcon = {
                                Icon(imageVector = Icons.Outlined.Report, contentDescription = null)
                            },
                        )
                        DropdownMenuItem(
                            modifier = Modifier
                                .toggleable(
                                    value = cat1Checked,
                                    interactionSource = cat1InteractionSource,
                                    indication = indication,
                                    role = Role.Switch,
                                    enabled = enabled,
                                ) {
                                    if (enabled) {
                                        cat1Checked = false
                                        haptic.toggle(it)
                                    }
                                },
                            text = { Text(text = "see less " + post.categories[0]) },
                            onClick = {
                                haptic.click()
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Category,
                                    contentDescription = null,
                                )
                            },
                            trailingIcon = {
                                Rn3Switch(
                                    checked = cat1Checked,
                                    contentDescription = null,
                                    interactionSource = cat1InteractionSource,
                                    onCheckedChange = null,
                                    enabled = enabled,
                                )
                            },
                        )
                        DropdownMenuItem(
                            modifier = Modifier
                                .toggleable(
                                    value = cat2Checked,
                                    interactionSource = cat2InteractionSource,
                                    indication = indication,
                                    role = Role.Switch,
                                    enabled = enabled,
                                ) {
                                    if (enabled) {
                                        cat2Checked = false
                                        haptic.toggle(it)
                                    }
                                },
                            text = { Text(text = "see less " + post.categories[0] + " about " + post.categories[1]) },
                            onClick = {
                                haptic.click()
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Category,
                                    contentDescription = null,
                                )
                            },
                            trailingIcon = {
                                Rn3Switch(
                                    checked = cat2Checked,
                                    contentDescription = null,
                                    interactionSource = cat2InteractionSource,
                                    onCheckedChange = null,
                                    enabled = enabled,
                                )
                            },
                        )
                    }
                }
            }
        }
        Column(modifier = Modifier.padding(Rn3TextDefaults.paddingValues)) {
            Text(
                text = post.content ?: "",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp),
                textAlign = TextAlign.Justify,
            )
            if (post.additionalInfos.isNotEmpty()) {
                if (post.postType == PostType.CONTACT) {
                    Row(
                        modifier = Modifier
                            .padding(Rn3SurfaceDefaults.paddingValues.only(TOP))
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        OutlinedButton(
                            enabled = enabled,
                            onClick = {
                                haptic.click()

                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("sms:${post.additionalInfos[0].second}")
                                }

                                context.startActivity(intent)
                            },
                        ) {
                            Text(text = post.additionalInfos[0].first)
                        }
                    }
                } else if (post.postType == PostType.POLL) {
                    Poll(post)
                }
            }
        }
    }
}