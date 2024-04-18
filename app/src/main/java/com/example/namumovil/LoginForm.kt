package com.example.namumovil

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.namumovil.auth.EmailAuth
import com.example.namumovil.auth.GoogleAuth
import com.example.namumovil.databinding.LoginformBinding
import com.example.namumovil.view.CreateAccount
import com.example.namumovil.view.RestartPassword

class LoginForm: AppCompatActivity() {
    private lateinit var binding: LoginformBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginformBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoogle.setOnClickListener {
            val intent = Intent(this, GoogleAuth::class.java)
            startActivity(intent)
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.txtEmail.text.toString()
            val password = binding.txtPassword.text.toString()
            val intent = Intent(this, EmailAuth::class.java)
            intent.putExtra("email",email)
            intent.putExtra("password", password)
            startActivity(intent)
        }
        binding.lblCreateAccount.setOnClickListener {
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }
        binding.lblRestartPassword.setOnClickListener {
            val intent = Intent(this, RestartPassword::class.java)
            startActivity(intent)
        }
    }

}
