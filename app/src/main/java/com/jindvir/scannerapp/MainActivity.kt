package com.jindvir.scannerapp

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPreferences : SharedPreferences
    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)

        val db = Firebase.firestore

        val user = findViewById<TextView>(R.id.userTv)
        val scanValue = findViewById<TextView>(R.id.scanValue)
        val scanBtn = findViewById<Button>(R.id.scanBtn)
        val sendBtn = findViewById<Button>(R.id.sendBtn)
        val logoutBtn = findViewById<Button>(R.id.logoutBtn)
        user.text = sharedPreferences.getString("email" , "Error")


        scanBtn.setOnClickListener {
            val scanner = GmsBarcodeScanning.getClient(this)

            scanner.startScan()
                .addOnSuccessListener {
                    val rawValue: String? = it.rawValue
                    scanValue.text = rawValue



                }
                .addOnCanceledListener {
                    // Task canceled
                }
                .addOnFailureListener { e ->
                    // Task failed with an exception
                }
        }

        sendBtn.setOnClickListener {
            val values = hashMapOf(
                "scanned value" to scanValue.text.toString(),
                "id" to auth.currentUser?.uid.toString()
            )

            db.collection("qrData").document("values")
                .set(values , SetOptions.merge())
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!")
                    Toast.makeText(
                        baseContext,
                        "Data Stored Successfully",
                        Toast.LENGTH_SHORT,
                    ).show()}
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }

        }

        logoutBtn.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this@MainActivity , SignInUser::class.java)
            startActivity(intent)
            finish()
        }
    }
}