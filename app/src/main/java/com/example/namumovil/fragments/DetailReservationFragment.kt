package com.example.namumovil.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.namumovil.R
import com.example.namumovil.data.Repo
import com.example.namumovil.databinding.FragmentDetailReservationBinding
import com.example.namumovil.model.Reserva
import com.example.namumovil.viewmodel.ReservaViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DetailReservationFragment : Fragment() {
    private var _binding: FragmentDetailReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var reserva: Reserva
    private lateinit var viewModel: ReservaViewModel
    companion object {
        const val ARG_RESERVA = "reserva"

        fun newInstance(reserva: Reserva): DetailReservationFragment {
            val fragment = DetailReservationFragment()
            // Crea un Bundle para pasar los argumentos
            val args = Bundle()
            args.putParcelable(ARG_RESERVA, reserva) // Asume que Reserva es Parcelable
            // Asigna los argumentos al fragmento
            fragment.arguments = args

            return fragment
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ReservaViewModel::class.java)
        // Obtener la reserva del Bundle
        reserva = arguments?.getParcelable(ARG_RESERVA) ?: Reserva()
        binding.etNameEdit.setText(reserva.nombres)
        binding.etPhoneEdit.setText(reserva.telefono)
        binding.etNumPeopleEdit.setText(reserva.cantidadPersonas.toString())
        binding.etDateEdit.setText(reserva.fechaReserva)
        binding.etTimeEdit.setText(reserva.horario)
        binding.etComentEdit.setText(reserva.comentarios)

        binding.btnUpdateReservation.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Actualizar Reserva")
                .setMessage("¿Deseas actualizar tus datos de reserva?")
                .setNegativeButton("No") { dialog, _ ->
                    val transaction = parentFragmentManager.beginTransaction()
                    transaction.replace(R.id.container, SeeReservationFragment())
                    transaction.commit()
                    dialog.dismiss()
                }
                .setPositiveButton("Sí") { dialog, _ ->
                    val newReserva = Reserva(
                        telefono = binding.etPhoneEdit.text.toString(),
                        cantidadPersonas = binding.etNumPeopleEdit.text.toString(),
                        fechaReserva = binding.etDateEdit.text.toString(),
                        horario = binding.etTimeEdit.text.toString(),
                        comentarios = binding.etComentEdit.text.toString()
                    )
                    val reservaId = reserva.reservaId
                    viewModel.updateReservaUser(reservaId, newReserva)
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Reserva Actualizada")
                        .setMessage("Se han actualizado los datos. Puedes regresar a la ventana anterior.")
                        .setPositiveButton("OK") { confirmDialog, _ ->
                            val transaction = parentFragmentManager.beginTransaction()
                            transaction.replace(R.id.container, SeeReservationFragment())
                            transaction.commit()
                            confirmDialog.dismiss()
                        }
                        .show()
                    dialog.dismiss()
                }
                .show()
        }
        binding.btnRegresar.setOnClickListener {
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.container, SeeReservationFragment())
            transaction.commit()
        }
    }

    private fun getStatusIndex(statusOptions: Array<String>, status: String): Int {
        val statusList = statusOptions.toList()
        return statusList.indexOf(status)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
