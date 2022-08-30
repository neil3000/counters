package rahmouni.neil.counters.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import rahmouni.neil.counters.R

@SuppressLint("IntentReset")
fun sendEmail(activity: Activity, address: String, subject: String, body: String? = null) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        type = "message/rfc822"
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        if (body!=null) putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        activity.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        // NoApp
        Toast.makeText(
            activity,
            activity.getString(R.string.commonIntents_sendEmail_toast_noApp),
            Toast.LENGTH_LONG
        ).show()
    }
}

fun openChromeCustomTab(activity: Activity, url: String) {
    CustomTabsIntent.Builder().build().launchUrl(
        activity,
        Uri.parse(url)
    )
}

fun openPlayStoreUrl(activity: Activity, url: String) {
    activity.startActivity(Intent(Intent.ACTION_VIEW).apply {
        data = Uri.parse(url)
        setPackage("com.android.vending")
    })
}
