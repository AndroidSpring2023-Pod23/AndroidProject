package com.example.quizzle

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtil {

    companion object {
        private const val TAG = "FirebaseUtil/"
        private val db = FirebaseFirestore.getInstance()

        fun addUser(username: String, quizzesTaken: Int, avgScore: Double) {
            val user = hashMapOf(
                "username" to username,
                "quizzesTaken" to quizzesTaken,
                "avgScore" to avgScore
            )

            db.collection("users")
                .document(username) // Use the username as the document ID
                .set(user) // Use set() instead of add() to update the existing document with the given ID
                .addOnSuccessListener {
                    Log.d(TAG, "User added with ID: $username")
                }
                .addOnFailureListener {
                    Log.w(TAG, "Error adding user", it)
                }
        }


        fun updateUser(userId: String, field: String, value: Any) {
            db.collection("users")
                .document(userId)
                .update(field, value)
                .addOnSuccessListener {
                    Log.d(TAG, "User $field updated successfully")
                }
                .addOnFailureListener {
                    Log.w(TAG, "Error updating user $field", it)
                }
        }

        fun getUserByUsername(username: String, onSuccess: ((Triple<String, Int, Double>) -> Unit)? = null, onFailure: ((Exception) -> Unit)? = null) {
            db.collection("users")
                .document(username)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val user = Triple(
                            document.getString("username") ?: "",
                            document.getLong("quizzesTaken")?.toInt() ?: 0,
                            document.getDouble("avgScore") ?: 0.0
                        )
                        onSuccess?.invoke(user)
                    } else {
                        onFailure?.invoke(NoSuchElementException("User with username $username not found."))
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure?.invoke(exception)
                }
        }


    }
}
