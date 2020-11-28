package com.example.ps

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SurveyCreation : AppCompatActivity() {

    private lateinit var createBtn: Button
    private lateinit var questionText: EditText
    private val db = Firebase.firestore
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.survey_creation)
        mAuth = FirebaseAuth.getInstance()

        createBtn = findViewById(R.id.createBtn)
        questionText = findViewById(R.id.editSurveyQuestions)

        createBtn.setOnClickListener{
            var questions = questionText.text.split("\n")
            if (questions.isEmpty()) {
                Toast.makeText(this, "Surveys need at least 1 question!", Toast.LENGTH_LONG).show()
            } else {
                val survey = hashMapOf(
                    "questions" to questions,
                    "user" to db.document("users/" + mAuth!!.currentUser?.email)
                )

                db.collection("surveys")
                    .add(survey)

                val intent = Intent(this, SurveyDashboard::class.java)
                setResult(Activity.RESULT_OK, intent)
                finish()
            }
        }
    }
}