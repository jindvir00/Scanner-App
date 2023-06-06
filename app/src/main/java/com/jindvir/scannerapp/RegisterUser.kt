package com.jindvir.scannerapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegisterUser : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var anim: LottieAnimationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_user)

        auth = Firebase.auth
        anim = findViewById(R.id.animationView)

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
        val btn = findViewById<Button>(R.id.registerBtn)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        val email = findViewById<EditText>(R.id.etEmail)
        val pass = findViewById<EditText>(R.id.etPass)

        loginBtn.setOnClickListener {
            val intent = Intent(this@RegisterUser , SignInUser::class.java)
            startActivity(intent)
            finish()
        }

        btn.setOnClickListener {
            anim.visibility = View.VISIBLE
            val emailVal = email.text.toString()
            val passVal = pass.text.toString()
            createUser(emailVal , passVal)
//            Toast.makeText(this@RegisterUser , emailVal, Toast.LENGTH_SHORT)
//                .show()
        }
    }

    private fun createUser(email: String , password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    val myEdit = sharedPreferences.edit()
                    if (user != null) {
                        myEdit.putString("email" , email)
                    }
                    myEdit.apply()
                    val intent = Intent(this@RegisterUser , SignInUser::class.java)
                    anim.visibility = View.GONE

                    startActivity(intent)
                    finish()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "failure registering user", task.exception)
                    Toast.makeText(
                        baseContext,
                        "Registration failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val intent = Intent(this@RegisterUser , MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}