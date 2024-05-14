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

package dev.rahmouni.neil.counters.core.common

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.google.android.gms.oss.licenses.OssLicensesActivity
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity

fun Context.openAndroidAccessibilitySettingsActivity() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.setClassName(
        "com.android.settings",
        "com.android.settings.Settings\$AccessibilitySettingsActivity",
    )
    ContextCompat.startActivity(this, intent, null)
}

fun Context.openLink(uri: Uri) {
    val intent = CustomTabsIntent.Builder()
    intent.setUrlBarHidingEnabled(true)
    intent.setShowTitle(false)
    intent.build().launchUrl(this, uri)
}

/**
 * In Android 12L and lower, there is no visual feedback when an app copies content to the clipboard, so we need to show a Toast to those users specifically, [as recommended by the Android docs](https://developer.android.com/develop/ui/views/touch-and-input/copy-paste#Feedback)
 */
fun Context.copyText(label: String, text: String) {
    (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText(label, text))

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        Toast.makeText(this, getString(R.string.core_common_copiedText_toast), Toast.LENGTH_SHORT).show()
    }
}

fun Context.openOssLicensesActivity() {
    startActivity(Intent(this, OssLicensesMenuActivity::class.java))
}