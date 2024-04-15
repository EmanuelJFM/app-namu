package com.example.namumovil.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.namumovil.R
import com.example.namumovil.databinding.FragmentUserConfigurationBinding
import com.example.namumovil.viewmodel.UserViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class UserConfigurationFragment : Fragment() {
    private var _binding: FragmentUserConfigurationBinding? = null
    private val binding get() = _binding!!
    private val viewModel: UserViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserConfigurationBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnUserUpdate.setOnClickListener {
            val newName = binding.etUserUpdate.text.toString()
            val newCell = binding.etPhoneUpdate.text.toString()

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Confirmación")
                .setMessage("¿Estás seguro de que quieres actualizar tus datos?")
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                    // Redirigir al fragmento Home usando NavController
                    findNavController().navigate(R.id.item_1)
                }
                .setPositiveButton("Sí") { dialog, _ ->
                    viewModel.updateUserData(newName, newCell,
                        onSuccess = {
                            // Mostrar un diálogo de confirmación
                            MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Éxito")
                                .setMessage("Tus datos han sido actualizados.")
                                .setPositiveButton("OK") { successDialog, _ ->
                                    successDialog.dismiss()
                                    // Redirigir al fragmento Home usando NavController
                                    findNavController().navigate(R.id.item_1)
                                }
                                .show()
                        },
                        onFailure = {
                        }
                    )
                    dialog.dismiss()
                }
                .show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
