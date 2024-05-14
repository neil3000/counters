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
import android.content.ComponentName
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsCallback
import androidx.browser.customtabs.CustomTabsClient
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsServiceConnection
import androidx.browser.customtabs.CustomTabsSession
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import dev.rahmouni.neil.counters.core.common.Rn3Uri.Available

private var mClient: CustomTabsClient? = null
private var mSession: CustomTabsSession? = null

private val mConnection = object : CustomTabsServiceConnection() {
    override fun onCustomTabsServiceConnected(name: ComponentName, client: CustomTabsClient) {
        mClient = client
        mClient?.warmup(0L)
        mSession = mClient?.newSession(CustomTabsCallback())
    }

    override fun onServiceDisconnected(name: ComponentName?) {
        mClient = null
        mSession = null
    }
}

fun Context.openAndroidAccessibilitySettingsActivity() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.setClassName(
        "com.android.settings",
        "com.android.settings.Settings\$AccessibilitySettingsActivity",
    )
    ContextCompat.startActivity(this, intent, null)
}

fun Context.openUri(uri: Rn3Uri, wasPreloaded: Boolean = true) {
    if (uri !is Available) return

    val cct = (if (wasPreloaded) CustomTabsIntent.Builder(mSession) else CustomTabsIntent.Builder())
        .setUrlBarHidingEnabled(true)
        .setShowTitle(false)
        .setSendToExternalDefaultHandlerEnabled(true)
        .build()

    cct.intent.putExtra(Intent.EXTRA_REFERRER, "android-app://${this.packageName}".toUri())
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        cct.intent.putExtra(Intent.EXTRA_PACKAGE_NAME, this.packageName)
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
        cct.intent.putExtra(Intent.EXTRA_REFERRER_NAME, "RahNeil_N3:Counters")
    }
    cct.launchUrl(this, uri.androidUri)
}

fun Context.prepareToOpenUri(uri: Rn3Uri) {
    if (uri !is Available) return

    this.initCCT()
    mSession?.mayLaunchUrl(uri.androidUri, null, null)
}

fun Context.initCCT() {
    Log.d("RahNeil_N3", "Initializing CCT")

    // Check for an existing connection
    if (mClient != null) {
        // Do nothing if there is an existing service connection
        return
    }

    // Get the default browser package name, this will be null if
    // the default browser does not provide a CustomTabsService
    val packageName = CustomTabsClient.getPackageName(this, null)

    if (packageName != null) {
        CustomTabsClient.bindCustomTabsService(this, packageName, mConnection)
    }
}

/**
 * In Android 12L and lower, there is no visual feedback when an app copies content to the clipboard, so we need to show a Toast to those users specifically, [as recommended by the Android docs](https://developer.android.com/develop/ui/views/touch-and-input/copy-paste#Feedback)
 */
fun Context.copyText(label: String, text: String) {
    (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(
        ClipData.newPlainText(
            label,
            text,
        ),
    )

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
        Toast.makeText(this, getString(R.string.core_common_copiedText_toast), Toast.LENGTH_SHORT)
            .show()
    }
}

fun Context.openOssLicensesActivity() {
    startActivity(Intent(this, OssLicensesMenuActivity::class.java))
}