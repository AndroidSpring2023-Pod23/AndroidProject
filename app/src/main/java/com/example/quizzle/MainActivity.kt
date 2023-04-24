package com.example.quizzle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONArray

private const val TAG = "Main Activity"
private const val SEARCH_URL = "https://the-trivia-api.com/api/questions?limit=20"
private val questions = mutableListOf<Question>()
class MainActivity : AppCompatActivity() {

    // quiz categories
    private lateinit var categoryCardViews: List<CardView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get category card views
        categoryCardViews = listOf(
            findViewById(R.id.general_card_view),
            findViewById(R.id.music_card_view),
            findViewById(R.id.science_card_view),
            findViewById(R.id.film_card_view),
            findViewById(R.id.sport_card_view),
            findViewById(R.id.geo_card_view)
        )
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
        startActivity(intent)
    }
}