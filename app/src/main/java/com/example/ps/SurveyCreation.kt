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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.survey_creation)
        mAuth = FirebaseAuth.getInstance()

        createBtn = findViewById(R.id.createBtn)
        questionText = findViewById(R.id.editSurveyQuestions)
        surveyTitle = findViewById(R.id.surveyTitle)

        createBtn.setOnClickListener{
            var questions = questionText.text.split("\n")
            if (questions.isEmpty() || surveyTitle.text.isBlank()) {
                Toast.makeText(this, "Surveys need at least 1 question!", Toast.LENGTH_LONG).show()
            } else {
                if (surveyTitle.text.matches(Regex("/^\\S*\$/"))) {
                    Toast.makeText(this, "Codes can't have any spaces!", Toast.LENGTH_LONG).show()
                } else {

                    db.collection("surveys")
                        .get()
                        .addOnSuccessListener { documents ->


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

                            if (!found) {
                                /*
                                    questions:
                                        question: {
                                            val: "blah"
                                            rating: 1
                                            ratings: 10
                                           }

                                 */
                                val questionList = ArrayList<Question>()
                                for (question in questions) {
                                    val temp = Rating(5, 1)
                                    val tempQ = Question(question, temp)
                                    questionList.add(tempQ)
                                }

                                val survey = hashMapOf(
                                    "questions" to questionList,
                                    "code" to surveyTitle.text.toString(),
                                    //"user" to db.document("users/" + mAuth!!.currentUser?.email)
                                    "user" to mAuth!!.currentUser?.email.toString()
                                )

                                db.collection("surveys")
                                    .add(survey)


                                val intent = Intent(this, MainActivity::class.java)
                                //setResult(Activity.RESULT_OK, intent)
                                //finish()
                                startActivity(intent)
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