package com.example.ps

import android.app.ListActivity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser

class SurveyDashboard  : AppCompatActivity() {

    private lateinit var createBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.survey_dashboard)

        createBtn = findViewById(R.id.createSurvey)

        createBtn.setOnClickListener{
            startActivityForResult(Intent(this, SurveyCreation::class.java), CREATE_SURVEY_REQUEST)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_SURVEY_REQUEST && resultCode == RESULT_OK) {
            Toast.makeText(this, "Created survey!", Toast.LENGTH_LONG).show()
        }

    }


    companion object {

        private val CREATE_SURVEY_REQUEST = 0
    }
}