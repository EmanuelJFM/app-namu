package com.example.namumovil.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.namumovil.LoginForm
import com.example.namumovil.R
import com.example.namumovil.databinding.FragmentHomeBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeFirebaseAuth()
        initializeGoogleSignInClient()

        binding.btnCerrar.setOnClickListener {
            signOutAndStartLoginFormActivity()
        }
    }

    private fun initializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance()
    }

    private fun initializeGoogleSignInClient() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
    }

    private fun signOutAndStartLoginFormActivity() {
        val user = mAuth.currentUser
        val providerId = user?.providerData?.get(1)?.providerId
        // Cierra la sesión en FirebaseAuth
        mAuth.signOut()
        // Si el proveedor de inicio de sesión es Google, cierra la sesión en GoogleSignInClient
        if (providerId == "google.com") {
            mGoogleSignInClient.signOut().addOnCompleteListener {
                // Inicia LoginForm Activity
                val intent = Intent(requireContext(), LoginForm::class.java)
                startActivity(intent)
                activity?.finish()
            }
        } else {
            // Inicia LoginForm Activity
            val intent = Intent(requireContext(), LoginForm::class.java)
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
