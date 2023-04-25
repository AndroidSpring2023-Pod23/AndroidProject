package com.example.quizzle

import android.util.Log
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class Question {
    @SerializedName("question")
    var question: String? = null

    @SerializedName("correctAnswer")
    var correctAnswer: String? = null

    @SerializedName("incorrectAnswers")
    var wrongAnswers: List<String>? = null

    private fun fromJson(jsonObject: JSONObject) : Question? {
        val q = Question()

        try {

            q.correctAnswer = jsonObject.getString("correctAnswer")
            q.question = jsonObject.getString("question")
            q.wrongAnswers = jsonObject.getString("incorrectAnswers").replace("[", "").replace("]", "").split(",").toMutableList()

        } catch (e: JSONException) {
            e.printStackTrace()
            return null
        }
        return q
    }

    companion object {
        fun fromJson(jsonArray: JSONArray): ArrayList<Question> {
            val questions = ArrayList<Question>()

            for (i in 0 until 10) {
                val jsonObject = jsonArray.getJSONObject(i)

                val q = Question().apply {
                    correctAnswer = jsonObject.getString("correctAnswer")
                    question = jsonObject.getString("question")
                    wrongAnswers = jsonObject.getJSONArray("incorrectAnswers")
                        .let { 0.until(it.length()).map { i -> it.getString(i) } }
                }

                questions.add(q)
            }
            Log.d("Question/", "${questions.size}")
            return questions
        }
    }
}