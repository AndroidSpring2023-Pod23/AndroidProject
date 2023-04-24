package com.example.quizzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.material.bottomnavigation.BottomNavigationView

class ResultsActivity : AppCompatActivity() {

    private lateinit var messageTV: TextView
    private lateinit var scoreTV: TextView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var ratingBar: RatingBar
    private var score: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        // get score
        score = intent.getIntExtra("score", 0)

        // get the views
        messageTV = findViewById(R.id.resultMessage)
        scoreTV = findViewById(R.id.scoreTV)
        bottomNav = findViewById(R.id.bottomNav)
        ratingBar = findViewById(R.id.ratingBar)

        // set bottom nav intents
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handle click on Home button
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_leaderboard -> {
                    // Handle click on Leaderboard button
                    /*val intent = Intent(this, LeaderboardActivity::class.java)
                    startActivity(intent)*/
                    true
                }
                else -> false
            }
        }

        setMessage()
        setScore()
    }

    private fun setMessage(){
        // TP-DO: switch between messages depending on score
        messageTV.text = "Congrats!"
    }

    private fun setScore(){
        scoreTV.text = "You scored $score out of 10 questions correct!"
        val maxScore = 10
        val maxRating = 3
        ratingBar.rating = ((score.toFloat() / maxScore.toFloat()) * maxRating.toFloat())
    }
}