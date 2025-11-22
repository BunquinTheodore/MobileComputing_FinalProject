package com.example.vibecoders_finalprojectmobilecomputing

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.analytics.FirebaseAnalytics

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var sharedPreferences: SharedPreferences
    private var isPasswordVisible = false

    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "MainActivity"
        private const val PREFS_NAME = "LoginPrefs"
        private const val KEY_REMEMBER_ME = "rememberMe"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent("app_started", null)

        // Configure Google Sign In
        val webClientId = getString(R.string.default_web_client_id)
        Log.d(TAG, "Configuring Google Sign-In with web client ID: $webClientId")

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .requestProfile()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
        Log.d(TAG, "Google Sign-In client initialized")

        // Check if user is already signed in or remembered
        val currentUser = auth.currentUser
        val rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER_ME, false)

        if (currentUser != null && rememberMe) {
            // User is signed in and remembered, go to hackathon list
            startActivity(Intent(this, HackathonListActivity::class.java))
            finish()
            return
        }

        // Load remember me state
        findViewById<CheckBox>(R.id.cbRememberMe).isChecked = rememberMe

        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Login button
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLogin).setOnClickListener {
            loginUser()
        }

        // Sign up link
        findViewById<android.widget.TextView>(R.id.tvSignUp).setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        // Google sign in button
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnGoogleSignIn).setOnClickListener {
            signInWithGoogle()
        }

        // Forgot password
        findViewById<android.widget.TextView>(R.id.tvForgotPassword).setOnClickListener {
            // TODO: Implement forgot password functionality
            Toast.makeText(this, "Forgot password functionality coming soon", Toast.LENGTH_SHORT).show()
        }

        // Password visibility toggle
        findViewById<ImageView>(R.id.ivPasswordToggle).setOnClickListener {
            togglePasswordVisibility()
        }
    }

    private fun togglePasswordVisibility() {
        val passwordField = findViewById<android.widget.EditText>(R.id.etPassword)
        val toggleIcon = findViewById<ImageView>(R.id.ivPasswordToggle)

        if (isPasswordVisible) {
            // Hide password
            passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            isPasswordVisible = false
        } else {
            // Show password
            passwordField.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            isPasswordVisible = true
        }

        // Move cursor to end
        passwordField.setSelection(passwordField.text.length)
    }
    
    private fun loginUser() {
        val email = findViewById<android.widget.EditText>(R.id.etUsername).text.toString().trim()
        val password = findViewById<android.widget.EditText>(R.id.etPassword).text.toString().trim()
        val rememberMe = findViewById<CheckBox>(R.id.cbRememberMe).isChecked

        if (email.isEmpty()) {
            findViewById<android.widget.EditText>(R.id.etUsername).error = "Email is required"
            return
        }

        if (password.isEmpty()) {
            findViewById<android.widget.EditText>(R.id.etPassword).error = "Password is required"
            return
        }

        // Sign in with Firebase Auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser

                    // Save remember me preference
                    sharedPreferences.edit().putBoolean(KEY_REMEMBER_ME, rememberMe).apply()

                    Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                    // Go to hackathon list
                    startActivity(Intent(this, HackathonListActivity::class.java))
                    finish()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(this, "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
    
    private fun signInWithGoogle() {
        Log.d(TAG, "Starting Google Sign-In")
        // Sign out first to ensure a clean sign-in flow
        googleSignInClient.signOut().addOnCompleteListener {
            Log.d(TAG, "Previous Google session cleared")
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    Log.d(TAG, "firebaseAuthWithGoogle: ${account.id}")
                    Log.d(TAG, "Account email: ${account.email}")
                    Log.d(TAG, "Account display name: ${account.displayName}")

                    if (account.idToken != null) {
                        firebaseAuthWithGoogle(account.idToken!!)
                    } else {
                        Log.e(TAG, "ID Token is null")
                        Toast.makeText(this, "Failed to get ID token from Google", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Log.e(TAG, "Google account is null")
                    Toast.makeText(this, "Failed to get Google account", Toast.LENGTH_SHORT).show()
                }
            } catch (e: ApiException) {
                Log.e(TAG, "Google sign in failed with status code: ${e.statusCode}", e)
                Log.e(TAG, "Error message: ${e.message}")

                val errorMessage = when (e.statusCode) {
                    10 -> "Developer error - Please check SHA-1 certificate in Firebase Console"
                    12501 -> "Sign in was cancelled"
                    12500 -> "Sign in failed - Check your internet connection"
                    else -> "Sign in failed: ${e.message}"
                }

                Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            }
        }
    }
    
    private fun firebaseAuthWithGoogle(idToken: String) {
        Log.d(TAG, "Authenticating with Firebase using Google token")

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val rememberMe = findViewById<CheckBox>(R.id.cbRememberMe).isChecked

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Log.d(TAG, "User: ${user?.email}, UID: ${user?.uid}")

                    // Save remember me preference
                    sharedPreferences.edit().putBoolean(KEY_REMEMBER_ME, rememberMe).apply()

                    Toast.makeText(this, "Welcome ${user?.displayName ?: user?.email}!", Toast.LENGTH_SHORT).show()

                    // Go to hackathon list
                    startActivity(Intent(this, HackathonListActivity::class.java))
                    finish()
                } else {
                    Log.e(TAG, "signInWithCredential:failure", task.exception)
                    val errorMsg = task.exception?.message ?: "Unknown error"
                    Toast.makeText(this, "Authentication failed: $errorMsg", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Firebase auth failed", exception)
                Toast.makeText(this, "Firebase auth error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}
