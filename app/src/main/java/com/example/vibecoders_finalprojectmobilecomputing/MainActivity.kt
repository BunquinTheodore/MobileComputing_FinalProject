package com.example.vibecoders_finalprojectmobilecomputing

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
    
    companion object {
        private const val RC_SIGN_IN = 9001
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        
        // Initialize Firebase Analytics
        firebaseAnalytics = FirebaseAnalytics.getInstance(this)
        firebaseAnalytics.logEvent("app_started", null)
        
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        
        // Check if user is already signed in
        val currentUser = auth.currentUser
        if (currentUser != null) {
            // User is signed in, go to hackathon list
            startActivity(Intent(this, HackathonListActivity::class.java))
            finish()
            return
        }
        
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
        findViewById<android.widget.ImageButton>(R.id.btnGoogle).setOnClickListener {
            signInWithGoogle()
        }
        
        // Forgot password
        findViewById<android.widget.TextView>(R.id.tvForgotPassword).setOnClickListener {
            // TODO: Implement forgot password functionality
            Toast.makeText(this, "Forgot password functionality coming soon", Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun loginUser() {
        val email = findViewById<android.widget.EditText>(R.id.etUsername).text.toString().trim()
        val password = findViewById<android.widget.EditText>(R.id.etPassword).text.toString().trim()
        
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
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w(TAG, "Google sign in failed", e)
                Toast.makeText(this, "Google sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
    
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(this, "Google sign in successful!", Toast.LENGTH_SHORT).show()
                    
                    // Go to hackathon list
                    startActivity(Intent(this, HackathonListActivity::class.java))
                    finish()
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
