package com.example.quizzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameET: EditText
    private lateinit var newProfileBtn: Button
    private lateinit var loginBtn: Button
    private val TAG = "LoginActivity/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Initialize views
        usernameET = findViewById(R.id.usernameET)
        newProfileBtn = findViewById(R.id.button)
        loginBtn = findViewById(R.id.button2)

        // Set click listeners for buttons
        newProfileBtn.setOnClickListener {
            // Check if the username is empty
            val username = usernameET.text.toString().lowercase()
            if (username.isEmpty()) {
                usernameET.error = "Please enter a username"
                return@setOnClickListener
            }
            // Add the new user to the Firebase database
            FirebaseUtil.addUser(username, 0,0.0, onSuccess = { user ->
                // Launch home activity with new user flag and user data
                launchHomeActivity(user)
            }, onFailure = { exception ->
                // Handle the failure case
                Toast.makeText(this, "Failed to add user: ${exception.message}", Toast.LENGTH_SHORT).show()
            })
        }

        loginBtn.setOnClickListener {
            // Fetch user details from database and launch home activity
            val username = usernameET.text.toString().lowercase()
            // Check if the username is empty
            if (username.isEmpty()) {
                usernameET.error = "Please enter a username"
                return@setOnClickListener
            }

            FirebaseUtil.getUserByUsername(username, onSuccess = { user ->
                // Launch home activity with existing user data
                launchHomeActivity(user)
            }, onFailure = { exception ->
                // Handle failure to fetch user data
                Toast.makeText(this, exception.message, Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun launchHomeActivity(user: Triple<String, Int, Double>? = null) {
        // Launch home activity with appropriate flags and user data as intent extras
        val intent = Intent(this, HomeActivity::class.java)
        if (user != null) {
            intent.putExtra("username", user.first)
            intent.putExtra("quizzesTaken", user.second)
            intent.putExtra("avgScore", user.third)
        }
        startActivity(intent)
        finish()
    }
}