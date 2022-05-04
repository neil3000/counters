package rahmouni.neil.counters.utils

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import rahmouni.neil.counters.R

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