package com.example.namumovil.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.namumovil.LoginForm
import com.example.namumovil.R
import com.example.namumovil.adapter.CarouselAdapter
import com.example.namumovil.data.Repo
import com.example.namumovil.databinding.FragmentHomeBinding
import com.example.namumovil.viewmodel.UserViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UserViewModel
    private lateinit var carouselAdapter: CarouselAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var repo: Repo

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
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        initializeFirebaseAuth()
        initializeGoogleSignInClient()
        val recyclerView = view.findViewById<RecyclerView>(R.id.carousel_recycler_view)
        repo = Repo()
        val arrayList = arrayListOf(
            "https://images.pexels.com/photos/357756/pexels-photo-357756.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
            "https://images.pexels.com/photos/5773996/pexels-photo-5773996.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
            "https://images.pexels.com/photos/3386854/pexels-photo-3386854.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1",
            "https://images.pexels.com/photos/5339080/pexels-photo-5339080.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"
        )

        carouselAdapter = CarouselAdapter(requireContext(), arrayList)
        recyclerView.adapter = carouselAdapter

        carouselAdapter.onItemClickListener = object : CarouselAdapter.OnItemClickListener {
            override fun onClick(imageView: ImageView, path: String) {
                val imageViewFragment = ImageViewFragment().apply {
                    arguments = Bundle().apply {
                        putString("image", path)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, imageViewFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }

        repo.getNumberOfReservations { numberOfReservations ->
            binding.tvVisitasNumero.text = numberOfReservations.toString()
        }

        binding.tvCerrarSesion.setOnClickListener {
            signOutAndStartLoginFormActivity()
        }
        viewModel.getCurrentUserData().observe(viewLifecycleOwner, Observer { user ->
            binding.tvUserName.text = user.name
            binding.tvUserEmail.text = user.email
        })
        binding.tvVerCarta.setOnClickListener {
            viewModel.descargarPdfYMostrarChooser("Fridays_Carta.pdf", requireContext())
        }
        binding.ivFacebook.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com"))
            startActivity(intent)
        }

        binding.ivInstagram.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com"))
            startActivity(intent)
        }

        binding.ivTwitter.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.twitter.com"))
            startActivity(intent)
        }
        binding.tvVerLocal.setOnClickListener {
            val lat = "-12.081989057826345"
            val lon = "-77.0356646841694"
            val url = "https://www.google.com/maps/search/?api=1&query=$lat,$lon"
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(mapIntent)
        }
        binding.tvActualizarCuenta.setOnClickListener {
            // Obtener el NavController
            val navController = findNavController()
            // Navegar al fragmento UserConfigurationFragment
            navController.navigate(R.id.userConfigurationFragment)
        }
        binding.tvQuejaSugerencia.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Queja o sugerencia")
                .setMessage("Para quejas o sugerencia, por favor remitir un correo a namuempresa@namu.com con el asunto\n" +
                        "y evidenia para realizar la correspondiente investigacion, mcuhas gracias")
                .setPositiveButton("Cerrar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        binding.tvContactarEmpresa.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Contactar con la empresa")
                .setMessage("Para contacatar a la empresa por reclamos o algun puesto laboral por favor\n" +
                        "comunicarse con +51 987 654 321, para más dudas o consultas, remita un correo en\n" +
                        "la seccion Queja o Sugerencia")
                .setNegativeButton("Correo electrónico") { dialog, _ ->
                    // Código para abrir el cliente de correo electrónico
                    dialog.dismiss()
                }
                .setPositiveButton("Llamada telefónica") { dialog, _ ->
                    // Código para iniciar una llamada telefónica
                    dialog.dismiss()
                }
                .show()
        }
    }
    //Google auth logout
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
