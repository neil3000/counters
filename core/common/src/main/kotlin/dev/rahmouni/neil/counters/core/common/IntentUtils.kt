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

fun Context.openAndroidAccessibilitySettingsActivity() {
    val intent = Intent(Intent.ACTION_MAIN)
    intent.setClassName(
        "com.android.settings",
        "com.android.settings.Settings\$AccessibilitySettingsActivity",
    )
    ContextCompat.startActivity(this, intent, null)
}

fun Context.openLink(url: String) {
    val intent = CustomTabsIntent.Builder()
    intent.setUrlBarHidingEnabled(true)
    intent.setShowTitle(false)
    intent.build().launchUrl(this, Uri.parse(url))
}

/**
 * In Android 12L and lower, there is no visual feedback when an app copies content to the clipboard, so we need to show a Toast to those users specifically, [as recommended by the Android docs](https://developer.android.com/develop/ui/views/touch-and-input/copy-paste#Feedback)
 */
fun Context.copyText(label: String, text: String) {
    (getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).setPrimaryClip(ClipData.newPlainText(label, text))

    if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
        Toast.makeText(this, "Copied", Toast.LENGTH_SHORT).show() //TODO i18n
}