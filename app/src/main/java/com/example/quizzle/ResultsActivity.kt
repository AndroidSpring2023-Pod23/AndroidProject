package com.example.quizzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class ResultsActivity : AppCompatActivity() {

    private lateinit var messageTV: TextView
    private lateinit var scoreTV: TextView
    private lateinit var bottomNav: BottomNavigationView
    private lateinit var ratingBar: RatingBar
    private lateinit var scoreTextView: TextView
    private lateinit var levelTextView: TextView
    private var score: Int = 0

    private lateinit var username: String
    private var quizzesTaken by Delegates.notNull<Int>()
    private var avgScore by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        username = intent.getStringExtra("username").toString()
        quizzesTaken = intent.getIntExtra("quizzesTaken",0)
        avgScore = intent.getDoubleExtra("avgScore", 0.0)

        // get score
        score = intent.getIntExtra("score", 0)

        // get the views
        messageTV = findViewById(R.id.resultMessage)
        scoreTV = findViewById(R.id.scoreTV)
        bottomNav = findViewById(R.id.bottomNav)
        ratingBar = findViewById(R.id.ratingBar)
        scoreTextView = findViewById(R.id.scoreTextView)
        levelTextView = findViewById(R.id.levelTextView)

        setMessage()
        setScore()

        // update average score and quizzes
        val quizzesTakenNew = quizzesTaken + 1
        val avgScoreNew = (avgScore * quizzesTaken + score) / quizzesTakenNew
        scoreTextView.text = String.format("%.1f", avgScoreNew)
        levelTextView.text = "$quizzesTakenNew"

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                updateData(username,quizzesTakenNew,avgScoreNew)
            }
        }

        // set bottom nav intents
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Handle click on Home button
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("username", username)
                    intent.putExtra("quizzesTaken", quizzesTakenNew)
                    intent.putExtra("avgScore", avgScoreNew)
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
    }

    private fun setMessage(){
        val text = if (score >= 7) {
            "Congrats!"
        } else if (score >= 4) {
            "Nice try!"
        } else {
            "Keep going!"
        }
        messageTV.text = text
    }

    private fun setScore(){
        scoreTV.text = "You scored $score out of 10 questions correct!"
        val maxScore = 10
        val maxRating = 3
        ratingBar.rating = ((score.toFloat() / maxScore.toFloat()) * maxRating.toFloat())
    }

    private fun updateData(userId:String, quizzesTaken: Int, avgScore: Double){
        FirebaseUtil.updateUser(userId, "quizzesTaken", quizzesTaken)
        FirebaseUtil.updateUser(userId, "avgScore", avgScore)
    }
}