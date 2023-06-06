package com.jindvir.scannerapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignInUser : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    private lateinit var auth: FirebaseAuth
    private lateinit var anim: LottieAnimationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in_user)

        val btn = findViewById<Button>(R.id.signInBtn)
        val email = findViewById<EditText>(R.id.etEmailSign)
        val pass = findViewById<EditText>(R.id.etPassSign)
        auth = Firebase.auth

        anim = findViewById(R.id.animationViewSignIn)

        btn.setOnClickListener {
            anim.visibility = View.VISIBLE
            val emailVal = email.text.toString()
            val passVal = pass.text.toString()
            signInUser(emailVal , passVal)
        }
    }

    private fun signInUser(email: String , password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    val intent = Intent(this@SignInUser , MainActivity::class.java)
                    anim.visibility = View.GONE

                            startActivity(intent)
                    finish()
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext,
                        "SignIn failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}