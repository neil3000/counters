# Keep CredentialManager (https://developer.android.com/identity/sign-in/credential-manager#proguard)
-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}