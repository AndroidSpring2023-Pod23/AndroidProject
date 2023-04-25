package com.example.quizzle

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import kotlin.properties.Delegates


class HomeActivity : AppCompatActivity() {

    private lateinit var categoryCardViews: List<CardView>
    private lateinit var usernameTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var levelTextView: TextView
    private lateinit var username: String
    private var quizzesTaken by Delegates.notNull<Int>()
    private var avgScore by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        usernameTextView = findViewById(R.id.usernameTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        levelTextView = findViewById(R.id.levelTextView)

        // get category card views
        categoryCardViews = listOf(
            findViewById(R.id.general_card_view),
            findViewById(R.id.music_card_view),
            findViewById(R.id.science_card_view),
            findViewById(R.id.film_card_view),
            findViewById(R.id.sport_card_view),
            findViewById(R.id.geo_card_view)
        )

        username = intent.getStringExtra("username").toString()
        quizzesTaken = intent.getIntExtra("quizzesTaken",0)
        avgScore = intent.getDoubleExtra("avgScore", 0.0)
        usernameTextView.text = "Hi $username,"
        scoreTextView.text = String.format("%.1f", avgScore)
        levelTextView.text = "$quizzesTaken"

        setCategoryListeners() // set the category card view listeners
    }

    // add on-click listener to each category
    private fun setCategoryListeners(){
        for (cardView in categoryCardViews) {
            cardView.setOnClickListener { onCategoryClicked(cardView.tag as String) }
        }
    }

    // on-click action for a category
    // launch the quiz activity
    private fun onCategoryClicked(category: String) {
        val intent = Intent(this, QuizActivity::class.java)
        intent.putExtra("category", category)
        intent.putExtra("username", username)
        intent.putExtra("quizzesTaken", quizzesTaken)
        intent.putExtra("avgScore", avgScore)
        startActivity(intent)
    }
}