package com.steamtofu.mejaku.db

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.steamtofu.mejaku.entity.student.StudentScores

object FirestoreHelper {
    private val user = HashMap<String, String>()

    private fun createNewUser(student: StudentScores){
        user["name"] = student.name
        user["studentMail"] = student.studentMail
        user["parentMail"] = student.parentMail
        user["quiz"] = student.quiz.toString()
        user["assignment1"] = student.assignment1.toString()
        user["assignment2"] = student.assignment2.toString()
        user["assignment3"] = student.assignment3.toString()
        user["scoresPrediction"] = student.scoresPrediction.toString()
    }

    fun insertData(student: StudentScores, firestoreDB: FirebaseFirestore){
        createNewUser(student)
        firestoreDB.collection("students")
            .add(user)
            .addOnSuccessListener { p0 ->
                Log.d(
                    "onSuccess",
                    "DocumentSnapshot added with ID: $p0"
                )
            }
            .addOnFailureListener { p0 -> Log.w("onFailure", "Error adding document", p0) }
    }

    fun readData(firestoreDB: FirebaseFirestore){
        firestoreDB.collection("students")
            .get()
            .addOnCompleteListener{
                if (it.isSuccessful){
                    for (document in it.result!!) {
                        Log.d("onSuccessful", "id: ${document.id} => ${document.data}")
                    }
                } else {
                    Log.w("onFailure", "Error getting documents.", it.exception)
                }
            }
    }
}