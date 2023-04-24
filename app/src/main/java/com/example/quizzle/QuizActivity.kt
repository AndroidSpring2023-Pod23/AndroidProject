package com.example.quizzle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONArray

class QuizActivity : AppCompatActivity() {

    private var questions = mutableListOf<Question>()
    private val TAG = "QuizActivity/"
    private val SEARCH_URL = "https://the-trivia-api.com/api/questions?limit=10"

    private lateinit var questionText: TextView
    private lateinit var optionA: TextView
    private lateinit var optionB: TextView
    private lateinit var optionC: TextView
    private lateinit var optionD: TextView
    private lateinit var nextButton: Button
    private lateinit var questionNumberText: TextView
    private var currentIndex = 0
    private var score = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)
        val category = intent.getStringExtra("category")?.lowercase() ?: "general"
        fetchQuiz(category)
        questionText = findViewById(R.id.questionTV)
        optionA = findViewById(R.id.option1TextView)
        optionB = findViewById(R.id.option2TextView)
        optionC = findViewById(R.id.option3TextView)
        optionD = findViewById(R.id.option4TextView)
        nextButton = findViewById(R.id.nextButton)
        questionNumberText = findViewById(R.id.numberTV)
    }

    // Retrieve quiz questions from API and store them in questions list
    private fun fetchQuiz(category: String){
        val client = AsyncHttpClient()
        val params = RequestParams()
        params["Content-Type"] = "application/json"
        params["categories"] = category
        client.get(SEARCH_URL, params, object: JsonHttpResponseHandler() {
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "Failed to fetch questions: $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JsonHttpResponseHandler.JSON) {
                Log.i(TAG, "Successful ${json.jsonArray}")
                val questionJSONArray: JSONArray? = json.jsonArray
                questionJSONArray?.let{Question.fromJson(it)}?.let {questions.addAll(it)}
                Log.i(TAG, "Question ${questions[0].question}")
                populateQuestion(questions[currentIndex])

                var quizEnded = false

                // Set click listener for next button
                nextButton.setOnClickListener {
                    currentIndex++
                    if (currentIndex < questions.size-1) {
                        populateQuestion(questions[currentIndex])
                    } else {
                        // Quiz is finished, display results or end quiz
                        populateQuestion(questions[currentIndex])
                        nextButton.text = "Finish"
                        quizEnded = true
                    }
                    if (quizEnded) {
                        nextButton.setOnClickListener {
                            val intent = Intent(this@QuizActivity, ResultsActivity::class.java)
                            intent.putExtra(
                                "score",
                                score
                            ) // Pass the score to the results activity
                            startActivity(intent)
                            finishActivity(0)
                        }
                    }
                }
            }
        })
    }

    // Update UI with question data
    private fun populateQuestion(question: Question) {
        resetOptions()
        setOptionListeners()
        questionNumberText.text = "Question ${currentIndex+1} out of 10"
        // Update question text
        questionText.text = question.question

        // Shuffle options
        val options = mutableListOf<String>().apply {
            question.correctAnswer?.let { add(it) }
            addAll(question.wrongAnswers ?: emptyList())
            shuffle()
        }

        // Update option text views
        optionA.text = options[0]
        optionB.text = options[1]
        optionC.text = options[2]
        optionD.text = options[3]
    }

    // Set option listeners
    private fun setOptionListeners() {
        // Initialize boolean variable to keep track if an option has been selected
        var isOptionSelected = false

        optionA.setOnClickListener {
            if (!isOptionSelected) {
                isOptionSelected = true
                if (optionA.text == questions[currentIndex].correctAnswer) {
                    score++
                    optionA.setBackgroundColor(ContextCompat.getColor(this, R.color.correct))
                } else {
                    optionA.setBackgroundColor(ContextCompat.getColor(this, R.color.wrong))
                    highlightCorrectOption()
                }
                disableOptions()
            }
        }

        optionB.setOnClickListener {
            if (!isOptionSelected) {
                isOptionSelected = true
                if (optionB.text == questions[currentIndex].correctAnswer) {
                    score++
                    optionB.setBackgroundColor(ContextCompat.getColor(this, R.color.correct))
                } else {
                    optionB.setBackgroundColor(ContextCompat.getColor(this, R.color.wrong))
                    highlightCorrectOption()
                }
                disableOptions()
            }
        }

        optionC.setOnClickListener {
            if (!isOptionSelected) {
                isOptionSelected = true
                if (optionC.text == questions[currentIndex].correctAnswer) {
                    score++
                    optionC.setBackgroundColor(ContextCompat.getColor(this, R.color.correct))
                } else {
                    optionC.setBackgroundColor(ContextCompat.getColor(this, R.color.wrong))
                    highlightCorrectOption()
                }
                disableOptions()
            }
        }

        optionD.setOnClickListener {
            if (!isOptionSelected) {
                isOptionSelected = true
                if (optionD.text == questions[currentIndex].correctAnswer) {
                    score++
                    optionD.setBackgroundColor(ContextCompat.getColor(this, R.color.correct))
                } else {
                    optionD.setBackgroundColor(ContextCompat.getColor(this, R.color.wrong))
                    highlightCorrectOption()
                }
                disableOptions()
            }
        }
    }

    // Highlight the correct option in light green
    private fun highlightCorrectOption() {
        when (questions[currentIndex].correctAnswer) {
            optionA.text -> optionA.setBackgroundColor(ContextCompat.getColor(this, R.color.correct))
            optionB.text -> optionB.setBackgroundColor(ContextCompat.getColor(this, R.color.correct))
            optionC.text -> optionC.setBackgroundColor(ContextCompat.getColor(this, R.color.correct))
            optionD.text -> optionD.setBackgroundColor(ContextCompat.getColor(this, R.color.correct))
        }
    }

    // Disable all options
    private fun disableOptions() {
        optionA.isClickable = false
        optionB.isClickable = false
        optionC.isClickable = false
        optionD.isClickable = false
    }

    // reset options
    private fun resetOptions() {
        optionA.apply {
            setBackgroundColor(ContextCompat.getColor(this@QuizActivity, R.color.options))
            isClickable = true
        }
        optionB.apply {
            setBackgroundColor(ContextCompat.getColor(this@QuizActivity, R.color.options))
            isClickable = true
        }
        optionC.apply {
            setBackgroundColor(ContextCompat.getColor(this@QuizActivity, R.color.options))
            isClickable = true
        }
        optionD.apply {
            setBackgroundColor(ContextCompat.getColor(this@QuizActivity, R.color.options))
            isClickable = true
        }
    }

=======
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

private var questionsB = mutableListOf<Question>()
class QuizActivity(): AppCompatActivity() {
    constructor(questionsA: MutableList<Question>) : this() {
        questionsB = questionsA
    }

    lateinit var questionText: TextView
    lateinit var optionA: TextView
    lateinit var optionB: TextView
    lateinit var optionC: TextView
    lateinit var optionD: TextView
    lateinit var confirmButton: Button

    lateinit var answers: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quiz)

        questionText = findViewById(R.id.questionText)
        optionA = findViewById(R.id.optionA)
        optionB = findViewById(R.id.optionB)
        optionC = findViewById(R.id.optionC)
        optionD = findViewById(R.id.optionD)
        confirmButton = findViewById(R.id.confirm_button)
        answers = ArrayList()


        for (i in 0 until questionsB.size) {
            loadQuestion(questionsB[i])
            confirmButton.setOnClickListener {
                return@setOnClickListener
            }
        }
    }

    private fun loadQuestion(questionP: Question) {
        questionText.text = questionP.question
        //optionA.text = "${resources.getString(R.string.a)}  ${questionP.correctAnswer}"
        answers.clear()
        questionP.correctAnswer?.let { answers.add(it) }
        for (i in questionP.wrongAnswers!!) {
            answers.add(i)
        }
        randomAssignment()
    }

    private fun randomAssignment() {
        val mixedAnswers = answers.shuffled()
        optionA.text = "${resources.getString(R.string.a)}  ${answers[0]}"
        optionB.text = "${resources.getString(R.string.b)}  ${answers[1]}"
        optionC.text = "${resources.getString(R.string.c)}  ${answers[2]}"
        optionD.text = "${resources.getString(R.string.d)}  ${answers[3]}"

    }
}