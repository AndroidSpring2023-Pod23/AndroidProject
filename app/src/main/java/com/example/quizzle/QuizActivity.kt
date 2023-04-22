package com.example.quizzle

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
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