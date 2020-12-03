package com.example.ps


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

        submitbutton.setOnClickListener {
            var i = 0
            var j: String = ""
            try {
                while (i < question.size) {
                    if(listViewQuestions.getItemAtPosition(i) == "Empty"){
                        throw Exception("Missing Selection")
                    }
                    j = (j).plus(" ").plus("Q").plus(i + 1).plus(":").plus(listViewQuestions.getItemAtPosition(i++))
                }
                var lstanswers = j.toString()
                var newfieldanswer = hashMapOf(
                    "answers" to lstanswers
                )
                db.collection("surveys")
                    .get()
                    .addOnSuccessListener { documents ->

                        for (document in documents) {
                            if (document["code"] == codes) {
                                db.collection("surveys").document(document.id)
                                    .set(newfieldanswer, SetOptions.merge())
                            }
                        }
                    }

                Toast.makeText(this,j,Toast.LENGTH_LONG).show()
            }catch (e: Exception){
                Toast.makeText(this,"Missing Selection",Toast.LENGTH_LONG).show()
            }

        }
        resetbutton.setOnClickListener{

            val questionListAdapter = QuestionList(this, question)
            listViewQuestions.adapter = questionListAdapter
        }

    }

}