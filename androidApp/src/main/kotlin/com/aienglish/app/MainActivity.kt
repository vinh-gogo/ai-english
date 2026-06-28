package com.aienglish.app

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.aienglish.core.designsystem.theme.AIEnglishTheme
import com.aienglish.feature.aiteacher.ui.screen.TeacherScreen
import com.aienglish.feature.profile.ui.viewmodel.ProfileViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val profileViewModel: ProfileViewModel by inject()

    private lateinit var googleSignInClient: GoogleSignInClient

    private val signInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account: GoogleSignInAccount = task.getResult(ApiException::class.java)
            val name = account.displayName ?: account.email?.substringBefore("@") ?: "Người dùng"
            val email = account.email ?: ""
            profileViewModel.onSignedIn(name, email)
        } catch (e: ApiException) {
            profileViewModel.onSignInError("Đăng nhập thất bại: ${e.statusCode}")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Configure Google Sign-In (request email + profile, no server auth code needed)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Check if already signed in (silent sign-in)
        val lastAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (lastAccount != null && !profileViewModel.state.value.isSignedIn) {
            profileViewModel.onSignedIn(
                lastAccount.displayName ?: lastAccount.email?.substringBefore("@") ?: "Người dùng",
                lastAccount.email ?: ""
            )
        }

        setContent {
            AIEnglishTheme {
                TeacherScreen(
                    onGoogleSignIn = {
                        signInLauncher.launch(googleSignInClient.signInIntent)
                    },
                    onGoogleSignOut = {
                        googleSignInClient.signOut()
                    }
                )
            }
        }
    }
}
