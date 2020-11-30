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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

private val db = Firebase.firestore
private lateinit var listViewQuestions: ListView
private lateinit var question:  List<String>

class Survey : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.survey_list)
        val intent = intent

        listViewQuestions = findViewById<View>(R.id.listViewQuestion) as ListView
        question = intent.getStringArrayListExtra("questions") as List<String>
        addQuestions()
    }
    private fun addQuestions(){

                val questionListAdapter = QuestionList(this, question)
                listViewQuestions.adapter = questionListAdapter
                    Log.d(TAG,"Document found")
    }
    companion object{
        const val TAG = "Project - MobileSurveySystem"
    }

}