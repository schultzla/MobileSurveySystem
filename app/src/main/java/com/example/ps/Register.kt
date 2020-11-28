package com.example.ps

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Register : AppCompatActivity() {
    private var reEmail: EditText? = null
    private var rePassword: EditText? = null
    private var reBt: Button? = null
    private var mAuth: FirebaseAuth? = null
    private var bar: ProgressBar? = null
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register)

        mAuth = FirebaseAuth.getInstance()

        reEmail = findViewById(R.id.email_re)
        rePassword = findViewById(R.id.password_re)
        reBt = findViewById(R.id.register_user)
        bar = findViewById(R.id.progressBar_re)

        bar?.visibility = View.INVISIBLE

        reBt!!.setOnClickListener {
            register()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean{
        if (password.isNullOrEmpty()) {
            return false
        }
        if(password.length < 8){
            return false
        }
        if(!password.matches(Regex(".*[0-9].*"))){
            return false
        }
        if(!password.matches(Regex(".*[a-zA-Z].*"))){
            return false
        }
        return true
    }

    private fun register() {
        bar!!.visibility = View.VISIBLE

        val email: String = reEmail!!.text.toString()
        val password: String = rePassword!!.text.toString()

        if(!isValidEmail(email)){
            Toast.makeText(applicationContext, "Please enter a valid email", Toast.LENGTH_LONG).show()
            bar!!.visibility = View.GONE
            return
        }
        if(!isValidPassword(password)){
            Toast.makeText(applicationContext, "Please enter a valid password", Toast.LENGTH_LONG).show()
            bar!!.visibility = View.GONE
            return
        }

        mAuth!!.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Registration successful!", Toast.LENGTH_LONG).show()
                    bar!!.visibility = View.GONE

                    val user = hashMapOf(
                        "email" to email
                    )

                    db.collection("users")
                        .document(email)
                        .set(user)
                        .addOnSuccessListener { documentReference ->
                            val intent = Intent(this, MainActivity::class.java)
                            intent.putExtra("user", mAuth!!.currentUser)
                            startActivity(intent)
                        }
                } else {
                    Toast.makeText(applicationContext, "Registration failed! Please try again later", Toast.LENGTH_LONG).show()
                    bar!!.visibility = View.GONE
                }
            }

    }
}