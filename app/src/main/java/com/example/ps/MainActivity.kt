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
import androidx.core.text.isDigitsOnly
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var loginBt: Button? = null
    private var registerBt: Button? = null
    private var startBt: Button? = null
    private var code:EditText? = null
    private val db = Firebase.firestore
    private var mAuth: FirebaseAuth? = null
    private lateinit var accountInstructions: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()

        var user = mAuth!!.currentUser

        if (user != null) {
            loginBt = findViewById<Button>(R.id.login)
            registerBt = findViewById<Button>(R.id.register)
            startBt = findViewById<Button>(R.id.start)
            code = findViewById<EditText>(R.id.code)
            accountInstructions = findViewById(R.id.accountInstructions)

            //code!!.inputType = InputType.TYPE_CLASS_NUMBER

            accountInstructions.text = "Welcome back ${user.email}!"

            loginBt!!.text = "My Surveys"
            registerBt!!.text = "Sign Out"

            registerBt!!.setOnClickListener{
                mAuth!!.signOut()
                finish()
                startActivity(intent)
            }

            loginBt!!.setOnClickListener{
                val intent = Intent(this, SurveyDashboard::class.java)
                intent.putExtra("user", mAuth!!.currentUser)
                startActivity(intent)
            }

            startBt!!.setOnClickListener {
                db.collection("surveys")
                    .get()
                    .addOnSuccessListener { documents ->


                        var found = false
                        for (document in documents) {
                            if (document["code"] == code!!.text.toString()) {
                                found = true
                                //start intent to do survey
                                //this means a survey was found corresponding to the code
                                //just need to add it as an extra to the intent and start an activity that involves completing the survey
                                val intent = Intent(this, Survey::class.java)
                                val lst =  document.get("questions")
                                intent.putStringArrayListExtra("questions",
                                    lst as ArrayList<String>?
                                )
                                startActivity(intent)
                            }
                        }
                        if (!found) {
                            Toast.makeText(this, "Invalid code", Toast.LENGTH_LONG).show()
                        }else {
                            Toast.makeText(this, "Survey code", Toast.LENGTH_LONG).show()
                        }


                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Invalid code", Toast.LENGTH_LONG).show()
                    }
            }
        } else {
            loginBt = findViewById<Button>(R.id.login)
            registerBt = findViewById<Button>(R.id.register)
            startBt = findViewById<Button>(R.id.start)
            code = findViewById<EditText>(R.id.code)

            //code!!.inputType = InputType.TYPE_CLASS_NUMBER

            loginBt!!.setOnClickListener {
                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            }
            registerBt!!.setOnClickListener {
                val intent = Intent(this, Register::class.java)
                startActivity(intent)
            }
            startBt!!.setOnClickListener {
                db.collection("surveys")
                    .get()
                    .addOnSuccessListener { documents ->
                        var found = false
                        for (document in documents) {
                            if (document.id == code!!.text.toString()) {
                                found = true
                                //start intent to do survey
                                //this means a survey was found corresponding to the code
                                //just need to add it as an extra to the intent and start an activity that involves completing the survey
                                val intent = Intent(this, Survey::class.java)
                                val lst =  document.get("questions")
                                intent.putStringArrayListExtra("questions",
                                    lst as ArrayList<String>?
                                )
                                startActivity(intent)
                            }
                        }
                        if (!found) {
                            Toast.makeText(this, "Invalid code", Toast.LENGTH_LONG).show()
                        }
                        else {
                            Toast.makeText(this, "Survey code", Toast.LENGTH_LONG).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this, "Invalid code", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }
}