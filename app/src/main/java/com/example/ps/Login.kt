package com.example.ps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.Validators
import android.text.InputType
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth

/*
    Login page
 */
class Login : AppCompatActivity() {
    private var loEmail: EditText? = null
    private var loPassword: EditText? = null
    private var loBt: Button? = null
    private var bar: ProgressBar? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        mAuth = FirebaseAuth.getInstance()
        loEmail = findViewById(R.id.email_login)
        loPassword = findViewById(R.id.password_login)
        loBt = findViewById(R.id.login_user)
        bar = findViewById(R.id.progressBar_login)

        bar?.visibility = View.INVISIBLE

        loBt!!.setOnClickListener {
            login()
        }
    }

    /*
        Logs a user in, does some validation to ensure a user has inputted items in the login fields
     */
    private fun login() {
        bar?.visibility = View.VISIBLE
        val email: String = loEmail?.text.toString()
        val password: String = loPassword?.text.toString()

        if(TextUtils.isEmpty(email)){
            Toast.makeText(applicationContext, "Please enter email", Toast.LENGTH_LONG).show()
            bar?.visibility = View.GONE
            return
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(applicationContext, "Please enter password", Toast.LENGTH_LONG).show()
            bar?.visibility = View.GONE
            return
        }

        mAuth!!.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener { task ->
                bar?.visibility = View.GONE
                if(task.isSuccessful){
                    Toast.makeText(applicationContext, "Login successful!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Log.i("MobileSurvey", task.exception.toString())
                    Toast.makeText(applicationContext, "Login failed!", Toast.LENGTH_LONG).show()
                }
            }

    }
}