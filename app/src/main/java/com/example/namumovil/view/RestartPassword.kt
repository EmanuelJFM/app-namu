package com.example.namumovil.view

import android.app.AlertDialog
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.namumovil.LoginForm
import com.example.namumovil.databinding.ActivityRestartPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class RestartPassword : AppCompatActivity() {
    private lateinit var binding: ActivityRestartPasswordBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestartPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        binding.btnRestartPassword.setOnClickListener {
            val email = binding.txtRestartEmail.text.toString()
            sendPasswordResetEmail(email)
        }
    }
    private fun sendPasswordResetEmail(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    AlertDialog.Builder(this)
                        .setTitle("Correo Enviado")
                        .setMessage("Se ha enviado un correo para restablecer su contraseña")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(this, LoginForm::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .show()
                } else {
                    Log.w(TAG, "Error sending password reset email", task.exception)
                    AlertDialog.Builder(this)
                        .setTitle("Error")
                        .setMessage("Ha ocurrido un error")
                        .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                        .show()
                }
            }
    }
}
