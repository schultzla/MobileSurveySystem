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
    private lateinit var surveyListAdapter: DashboardList
    private lateinit var codeList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.survey_dashboard)

        createBtn = findViewById(R.id.createSurvey)
        listViewSurvey = findViewById<View>(R.id.listViewSurvey) as ListView

        codeList = if (intent.getStringArrayListExtra("codeList").isNullOrEmpty()) {
            mutableListOf()
        } else {
            intent.getStringArrayListExtra("codeList")!!.toMutableList()
        }

        surveyListAdapter = DashboardList(this, codeList)
        listViewSurvey.adapter = surveyListAdapter

        listViewSurvey.setOnItemClickListener { adapterView, view, i, l ->
            db.collection("surveys")
                .get()
                .addOnSuccessListener { documents ->
                    for(document in documents){
                        if(document["code"] == codeList[i]){
                            val questions = document["questions"] as ArrayList<*>
                            val text = questions.toString()
                            val sub = text.replace("}", "")
                            val sub2 = sub.replace("{", "")
                            val sub3 = sub2.replace("[", "")
                            val sub4 = sub3.replace("]", "")
                            val sub5 = sub4.replace("rating=rating=", "rating=")
                            val sub6 = sub5.replace("=", ": ")
                            val final = sub6.replace(",", "\n")
                            val dialogBuilder = AlertDialog.Builder(this)
                            dialogBuilder.setMessage(final)
                                .setTitle("Your survey result")
                            val alert = dialogBuilder.create()
                            alert.show()
                        }
                    }
                }
        }

        listViewSurvey.setOnItemLongClickListener { parent, view, position, id ->
            val dialogBuilder = AlertDialog.Builder(this)
            dialogBuilder.setMessage("Do you want to delete this survey?")
                .setCancelable(false)
                .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                    db.collection("surveys")
                        .get()
                        .addOnSuccessListener { documents ->
                            for (document in documents) {
                                if (document["code"] == codeList[position]) {
                                    db.collection("surveys").document(document.id).delete()
                                        .addOnSuccessListener {
                                            codeList.removeAt(position)
                                            surveyListAdapter.notifyDataSetChanged()
                                        }

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
                db.collection("surveys")
                    .get()
                    .addOnSuccessListener { documents ->
                        codeList.clear()
                        val user = intent.extras?.get("user") as FirebaseUser
                        for (document in documents) {
                            if (document["user"] == user.email){
                                codeList.add(document["code"].toString())
                            }
                        }
                        Log.i("MobileSurvey", codeList.toString())
                        surveyListAdapter.notifyDataSetChanged()
                    }
            }

        }


        companion object {

            private val CREATE_SURVEY_REQUEST = 0
        }
    }