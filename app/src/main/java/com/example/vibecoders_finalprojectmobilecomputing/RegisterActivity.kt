package com.example.vibecoders_finalprojectmobilecomputing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class RegisterActivity : AppCompatActivity() {
    
    private lateinit var auth: FirebaseAuth
    
    companion object {
        private const val TAG = "RegisterActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        // Register button
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnRegister).setOnClickListener {
            registerUser()
        }
    }
    
    private fun registerUser() {
        val fullName = findViewById<EditText>(R.id.etFullName).text.toString().trim()
        val email = findViewById<EditText>(R.id.etEmail).text.toString().trim()
        val password = findViewById<EditText>(R.id.etPassword).text.toString().trim()
        val confirmPassword = findViewById<EditText>(R.id.etConfirmPassword).text.toString().trim()
        
        // Validation
        if (fullName.isEmpty()) {
            findViewById<EditText>(R.id.etFullName).error = "Full name is required"
            return
        }
        
        if (email.isEmpty()) {
            findViewById<EditText>(R.id.etEmail).error = "Email is required"
            return
        }
        
        if (password.isEmpty()) {
            findViewById<EditText>(R.id.etPassword).error = "Password is required"
            return
        }
        
        if (password.length < 6) {
            findViewById<EditText>(R.id.etPassword).error = "Password must be at least 6 characters"
            return
        }
        
        if (password != confirmPassword) {
            findViewById<EditText>(R.id.etConfirmPassword).error = "Passwords do not match"
            return
        }
        
        // Create user with Firebase Auth
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    
                    // Update user profile with display name
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(fullName)
                        .build()
                    
                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { profileTask ->
                            if (profileTask.isSuccessful) {
                                Log.d(TAG, "User profile updated.")
                            }
                        }
                    
                    Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                    
                    // Go to hackathon list
                    startActivity(Intent(this, HackathonListActivity::class.java))
                    finish()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this, "Registration failed: ${task.exception?.message}", 
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}
