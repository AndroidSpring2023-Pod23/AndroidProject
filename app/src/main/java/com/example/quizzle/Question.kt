package com.example.quizzle

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

    fun fromJson(jsonArray: JSONArray): ArrayList<Question> {
        var questionJson: JSONObject
        val questions = ArrayList<Question>()

        for ( i in 0 until 10) {
            questionJson = try {
                jsonArray.getJSONObject(i)
            } catch (e: Exception) {
                e.printStackTrace()
                continue
            }
            val question : Question? = fromJson(questionJson)
            if (question != null) {
                questions.add(question)
            }
        }
        return questions
    }
}