package com.example.namumovil.view

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.namumovil.databinding.ActivityCreateAccountBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CreateAccount : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnCreate.setOnClickListener {
            val name = binding.txtName.text.toString()
            val lastname = binding.txtLastname.text.toString()
            val phone = binding.txtPhone.text.toString()
            val email = binding.txtEmail.text.toString()
            val password = binding.txtNewPassword.text.toString()
            val confirmPassword = binding.txtConfirmPassword.text.toString()

            if (password == confirmPassword) {
                createAccount(name, lastname, phone, email, password)
            } else {
                AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage("Las contraseñas no coinciden")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
            }
        }
    }

    private fun createAccount(name: String, lastname: String, phone: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Guarda la información adicional del usuario en Firestore
                    val db = Firebase.firestore
                    val userMap = hashMapOf(
                        "name" to name,
                        "lastname" to lastname,
                        "phone" to phone,
                        "email" to email
                    )
                    db.collection("users").add(userMap)
                        .addOnSuccessListener { documentReference ->
                            Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                        }
                        .addOnFailureListener { e ->
                            Log.w(TAG, "Error adding document", e)
                        }
                    updateUI(auth.currentUser)
                } else {
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Error al crear la cuenta")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                    updateUI(null)
                }
            }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
