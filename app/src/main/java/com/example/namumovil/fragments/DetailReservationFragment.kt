package com.example.namumovil.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.namumovil.R
import com.example.namumovil.data.Repo
import com.example.namumovil.databinding.FragmentDetailReservationBinding
import com.example.namumovil.model.Reserva

class DetailReservationFragment : Fragment() {
    private var _binding: FragmentDetailReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var reserva: Reserva

    companion object {
        const val ARG_RESERVA = "reserva"
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

        // Obtener la reserva del Bundle
        reserva = arguments?.getSerializable(ARG_RESERVA) as? Reserva ?: Reserva()
        binding.etName.setText(reserva.nombres)
        binding.etPhone.setText(reserva.telefono)
        binding.etNumPeople.setText(reserva.cantidadPersonas.toString())
        binding.etDate.setText(reserva.fechaReserva)
        binding.etTime.setText(reserva.horario)
        binding.etComments.setText(reserva.comentarios)
        binding.etTicket.setText(reserva.ticket.toString())
        // Obtener el array de opciones de estado
        val statusArray = resources.getStringArray(R.array.status_options)
        // Obtener el índice del estado de la reserva en la lista de opciones de estado
        val statusIndex = getStatusIndex(statusArray, reserva.estado)
        // Establecer la selección del Spinner
        binding.spinnerStatus.setSelection(statusIndex)
        binding.btnCerrar.setOnClickListener {
            activity?.finish()
        }
        binding.btnEditar.setOnClickListener {
            val newEstado = binding.spinnerStatus.selectedItem.toString()
            val repo = Repo()
            repo.updateReservaEstado(reserva.id, newEstado,
                onSuccess = {
                    // Por ejemplo, mostrar un mensaje de éxito
                    Toast.makeText(requireContext(), "Estado actualizado correctamente", Toast.LENGTH_SHORT).show()
                    // También podrías finalizar la actividad si lo deseas
                    activity?.finish()
                },
                onFailure = {
                    // Por ejemplo, mostrar un mensaje de error
                    Toast.makeText(requireContext(), "Error al actualizar el estado", Toast.LENGTH_SHORT).show()
                }
            )
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
