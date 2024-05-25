package dev.rahmouni.neil.counters.core.auth.user

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.LoggedOutUser
import dev.rahmouni.neil.counters.core.auth.user.Rn3User.SignedInUser

sealed interface Rn3User {
    data object LoggedOutUser : Rn3User
    data class SignedInUser(
        val uid: String,
        internal val displayName: String,
        internal val pfpUri: Uri?,
    ) : Rn3User

    fun getDisplayName(): String {
        return when (this) {
            LoggedOutUser -> "Not signed in" //TODO i18n
            is SignedInUser -> displayName
        }
    }
}

internal fun FirebaseUser?.toRn3User(): Rn3User {
    return when (this) {
        null -> LoggedOutUser
        else -> SignedInUser(
            uid = this.uid,
            displayName = this.displayName.toString(),
            pfpUri = this.photoUrl,
        )
    }
}