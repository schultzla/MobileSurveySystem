package com.example.ps

import android.app.ListActivity
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SurveyDashboard : AppCompatActivity() {

    private lateinit var createBtn: Button
    private lateinit var listViewSurvey: ListView
    private val db = Firebase.firestore
    private var code = ArrayList<String>()
    private lateinit var text: String
    private lateinit var text2: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.survey_dashboard)

        createBtn = findViewById(R.id.createSurvey)
        listViewSurvey = findViewById<View>(R.id.listViewSurvey) as ListView

        code = intent.getStringArrayListExtra("codeList")!!
        code.add(0, "Create your survey by button below")
        var codeList = code.toList() as MutableList
        val surveyListAdapter = DashboardList(this, codeList)
        listViewSurvey.adapter = surveyListAdapter

        listViewSurvey.setOnItemLongClickListener { parent, view, position, id ->
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Do you want to delete this survey?")
                .setCancelable(false)
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    db.collection("surveys")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                if (document["code"] == code[position]) {
                                    db.collection("surveys").document(document.id).delete()
                                    codeList.removeAt(position)
                                    surveyListAdapter.notifyDataSetChanged()
                                }
                                if (code.isEmpty()) {
                                    break
                                }
                            }
                        }
                })
                .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
            val alert = dialogBuilder.create()
            alert.show()
            return@setOnItemLongClickListener true
        }


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