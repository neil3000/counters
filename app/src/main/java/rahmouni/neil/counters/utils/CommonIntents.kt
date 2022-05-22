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
fun sendEmail(activity: Activity, address: String, subject: String) {
    val intent = Intent(Intent.ACTION_SENDTO).apply {
        type = "message/rfc822"
        data = Uri.parse("mailto:")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
        putExtra(Intent.EXTRA_SUBJECT, subject)
    }

    try {
        activity.startActivity(intent)
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            activity,
            activity.getString(R.string.error_noAppToSendEmails),
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