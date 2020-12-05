package com.example.ps


import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.ListView
import androidx.core.text.isDigitsOnly
import androidx.core.view.get
import androidx.core.view.size
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

/*
    Page to take a survey
 */

class Survey : AppCompatActivity() {

    private lateinit var listViewQuestions: ListView
    private lateinit var submitbutton: Button
    private lateinit var resetbutton: Button
    private lateinit var question:  List<String>
    private lateinit var codes: String
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.survey_list)


        listViewQuestions = findViewById<View>(R.id.listViewQuestion) as ListView
        submitbutton = findViewById<Button>(R.id.submitbutton) as Button
        resetbutton = findViewById(R.id.resetbutton) as Button
        question = intent.getStringArrayListExtra("questions") as List<String>
        codes = intent.getStringExtra("codes") as String
        val questionListAdapter = QuestionList(this, question)
        listViewQuestions.adapter = questionListAdapter
        Log.d("TAG","Document found")

        /*
        On submit, we get all of the answers, check to make sure the user did answer all questions, and then calculate new average rating for each question
         */
        submitbutton.setOnClickListener {
            var i = 0
            var answers = arrayListOf<Int>()

            try {
                while (i < question.size) {
                    if(listViewQuestions.getItemAtPosition(i) == "Empty"){
                        throw Exception("Missing Selection")
                    }
                    val temp = listViewQuestions.getItemAtPosition(i++) as String
                    answers.add(temp.toInt())
                }

                db.collection("surveys")
                    .get()
                    .addOnSuccessListener { documents ->

                        for (document in documents) {
                            /*
                            Found the right survey
                             */
                            if (document["code"] == codes) {
                                val lst = document.get("questions") as ArrayList<*>

                                var i = 0
                                val questionList = ArrayList<Question>()

                                /*
                                For each question, we keep a rolling average of the current average rating
                                so here we are just updating this rolling average with the new ratings
                                 */
                                for (q in lst) {
                                    val temp = q as HashMap<*, *>
                                    val tempRating = temp["rating"] as HashMap<String, Long>
                                    val rating = Rating(tempRating["rating"]!!.toInt(), tempRating["ratingCount"]!!.toInt())

                                    var largeRating = rating.rating * rating.ratingCount
                                    largeRating += answers[i]

                                    rating.ratingCount = rating.ratingCount + 1
                                    rating.rating = largeRating / rating.ratingCount

                                    val tempQ = Question(temp["question"] as String, rating)
                                    questionList.add(tempQ)

                                    i += 1
                                }

                                val newData = hashMapOf("questions" to questionList)
                                db.collection("surveys").document(document.id).set(newData, SetOptions.merge())
                            }
                        }
                    }

                val intent = Intent(this, MainActivity::class.java)
                setResult(Activity.RESULT_OK, intent)
                finish()
            } catch (e: Exception){
                Toast.makeText(this,"Missing Selection",Toast.LENGTH_LONG).show()
            }

        }
        resetbutton.setOnClickListener{

            val questionListAdapter = QuestionList(this, question)
            listViewQuestions.adapter = questionListAdapter
        }

    }

}
