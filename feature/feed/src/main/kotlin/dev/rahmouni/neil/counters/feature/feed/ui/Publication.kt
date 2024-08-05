package dev.rahmouni.neil.counters.feature.feed.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.rahmouni.neil.counters.core.data.model.FriendEntity
import dev.rahmouni.neil.counters.core.designsystem.component.Rn3IconButton
import dev.rahmouni.neil.counters.core.designsystem.component.getHaptic
import dev.rahmouni.neil.counters.core.designsystem.rebased.Post
import dev.rahmouni.neil.counters.core.designsystem.rebased.PostType
import dev.rahmouni.neil.counters.core.designsystem.rebased.SharingScope
import dev.rahmouni.neil.counters.core.designsystem.rebased.text
import dev.rahmouni.neil.counters.core.model.data.Country

@Composable
fun Publication(post: Post, friendEntityRepository: List<FriendEntity>, enabled: Boolean) {
    val haptic = getHaptic()
    val context = LocalContext.current

    val friend = post.userId.let { id ->
        friendEntityRepository.find { it.uid == id }
    }

    Row(modifier = Modifier.padding(start = 16.dp, end= 16.dp, top = 2.dp, bottom = 16.dp)) {
        Surface(modifier = Modifier.padding(top = 14.dp)) {
            if (friend != null) {
                Icon(
                    imageVector = Icons.Outlined.AccountCircle,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(6.dp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                )
            } else {
                post.sharingScope.DisplayIcon(post.location)
            }
        }
        Column(modifier = Modifier.padding(start = 12.dp), verticalArrangement = Arrangement.spacedBy(
            (-12).dp
        )) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(modifier = Modifier.padding(end = 8.dp), verticalAlignment = Alignment.Bottom) {
                    if (friend != null) {
                        Text(text = friend.name ?: friend.phone?.getFormatedNumber() ?: "", fontWeight = FontWeight.Bold)
                    } else {
                        if (post.sharingScope == SharingScope.COUNTRY) {
                            Text(
                                text = Country.getCountryFromIso(post.location)?.text()
                                    ?: "Country not found",
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        else {
                            Text(text = post.location, fontWeight = FontWeight.Bold)
                        }
                    }
                    Text(
                        text = post.timeElapsed(),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .alpha(0.5f)
                            .padding(start = 6.dp, bottom = 1.dp),
                    )
                }
                Rn3IconButton(icon = Icons.Default.MoreHoriz, contentDescription = "Action on the publication") {}
            }
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(end = 8.dp),
            )
            if (post.additionalInfos.isNotEmpty()) {
                if (post.postType == PostType.CONTACT) {
                    Row(
                        modifier = Modifier
                            .padding(top = 14.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        post.additionalInfos.forEach { info ->
                            Button(
                                enabled = enabled,
                                onClick = {
                                    haptic.click()

                                    val intent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("sms:${info}")
                                    }

                                    context.startActivity(intent)
                                },
                            ) {
                                Text(info)
                            }
                        }
                    }
                } else if (post.postType == PostType.POLL) {
                    Poll(post)
                }
            }
        }
    }
}
