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
    private lateinit var surveyTitle: EditText
    private val db = Firebase.firestore
    private var mAuth: FirebaseAuth? = null

    /*
    Survey creation page, there's two inputs one for the questions and one for the survey code
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.survey_creation)
        mAuth = FirebaseAuth.getInstance()

        createBtn = findViewById(R.id.createBtn)
        questionText = findViewById(R.id.editSurveyQuestions)
        surveyTitle = findViewById(R.id.surveyTitle)

        /*
        On create we split the questions text to make an array of questions

        We also check to make sure a survey code was inputted and it doesn't hsave spaces
         */
        createBtn.setOnClickListener{
            var questions = questionText.text.split("\n")
            when {
                questionText.length() == 0 -> {
                    Toast.makeText(this, "Surveys need at least 1 question!", Toast.LENGTH_LONG).show()
                }
                surveyTitle.text.isBlank() -> {
                    Toast.makeText(this, "Please enter a survey code!", Toast.LENGTH_LONG).show()
                }
                !surveyTitle.text.matches(Regex("^\\S+$")) -> {
                    Toast.makeText(this, "Codes can't have any spaces!", Toast.LENGTH_LONG).show()
                }
                else -> {
                    db.collection("surveys")
                        .get()
                        .addOnSuccessListener { documents ->

                            /*
                            Survey codes need to be unique, since users type them in to access a survey, so we check if this survey code has been used before
                             */
                            var found = false
                            for (document in documents) {
                                if (document["code"] == surveyTitle.text.toString()) {
                                    found = true
                                    Toast.makeText(
                                        this,
                                        "This code has already been used! Please try another",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }

                            /*
                            If it hasn't been used, we create a question object list
                             */
                            if (!found) {
                                val questionList = ArrayList<Question>()
                                for (question in questions) {
                                    val temp = Rating(5, 1)
                                    val tempQ = Question(question, temp)
                                    questionList.add(tempQ)
                                }

                                val survey = hashMapOf(
                                    "questions" to questionList,
                                    "code" to surveyTitle.text.toString(),
                                    "user" to mAuth!!.currentUser?.email.toString()
                                )

                                db.collection("surveys")
                                    .add(survey)


                                val intent = Intent(this, MainActivity::class.java)
                                setResult(Activity.RESULT_OK, intent)
                                finish()
                            }
                        }
                }
            }
        }
    }
}

class Rating(rating: Int, ratingCount: Int) {
    var rating: Int = rating
    var ratingCount: Int = ratingCount
}

class Question(question: String, rating: Rating) {
    var question: String = question
    var rating: Rating = rating
}