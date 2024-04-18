package com.example.namumovil.fragments

import android.os.Bundle
import android.text.InputFilter
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.namumovil.R
import com.example.namumovil.data.InputFilterMinMax
import com.example.namumovil.data.Repo
import com.example.namumovil.databinding.FragmentDetailReservationBinding
import com.example.namumovil.model.Reserva
import com.example.namumovil.viewmodel.ReservaViewModel
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class DetailReservationFragment : Fragment() {
    private var _binding: FragmentDetailReservationBinding? = null
    private val binding get() = _binding!!
    private lateinit var reserva: Reserva
    private lateinit var viewModel: ReservaViewModel
    private lateinit var etTableEdit: AutoCompleteTextView
    private lateinit var etTimeEdit: AutoCompleteTextView
    private lateinit var repo: Repo
    companion object {
        const val ARG_RESERVA = "reserva"
        fun newInstance(reserva: Reserva): DetailReservationFragment {
            val fragment = DetailReservationFragment()
            val args = Bundle()
            args.putParcelable(ARG_RESERVA, reserva)
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
        val datePicker = createDatePicker()
        binding.etNumPeopleEdit.filters = arrayOf<InputFilter>(InputFilterMinMax(1, 10))
        viewModel = ViewModelProvider(this).get(ReservaViewModel::class.java)
        // Obtener la reserva del Bundle
        reserva = arguments?.getParcelable(ARG_RESERVA) ?: Reserva()
        binding.etNameEdit.setText(reserva.nombres)
        binding.etTableEdit.setText(reserva.mesa)
        binding.etPhoneEdit.setText(reserva.telefono)
        binding.etNumPeopleEdit.setText(reserva.cantidadPersonas.toString())
        binding.etDateEdit.setText(reserva.fechaReserva)
        binding.etTimeEdit.setText(reserva.horario)
        binding.etComentEdit.setText(reserva.comentarios)
        etTableEdit = view.findViewById(R.id.et_TableEdit)
        etTimeEdit = view.findViewById(R.id.et_TimeEdit)
        datePicker.addOnPositiveButtonClickListener {
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = sdf.format(Date(it))
            binding.etDateEdit.setText(date)
        }
        repo = Repo()
        repo.getHorarios({ horariosMap ->
            val ids = horariosMap.keys.toList()
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, ids)
            etTableEdit.setAdapter(adapter)
            etTableEdit.setOnItemClickListener { _, _, position, _ ->
                val selectedId = ids[position]
                val horarios = horariosMap[selectedId]
                if (horarios != null) {
                    repo.getReservasForDateAndTable(binding.etDateEdit.text.toString(), selectedId).observe(viewLifecycleOwner) { reservas ->
                        // Obtener los horarios reservados que no están cancelados
                        val horariosReservados = reservas.filter { it.estado != "Cancelado" }.map { it.horario }
                        // Filtrar los horarios disponibles
                        val horariosDisponibles = horarios.filter { !horariosReservados.contains(it) }
                        // Actualizar el adaptador de horarios
                        val timeAdapter = ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_dropdown_item_1line,
                            horariosDisponibles
                        )
                        etTimeEdit.setAdapter(timeAdapter)
                    }
                }
            }
        }, {
            Toast.makeText(context, "Error al obtener los horarios", Toast.LENGTH_SHORT).show()
        })
        binding.etDateEdit.setOnClickListener {
            datePicker.show(childFragmentManager, "datePicker")
        }
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
    private fun createDatePicker(): MaterialDatePicker<Long> {
        val today = MaterialDatePicker.todayInUtcMilliseconds()
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.timeInMillis = today
        calendar[Calendar.MONTH] = Calendar.JANUARY
        val janThisYear = calendar.timeInMillis
        calendar[Calendar.MONTH] = Calendar.DECEMBER
        val decThisYear = calendar.timeInMillis
        val constraintsBuilder = CalendarConstraints.Builder()
            .setStart(janThisYear)
            .setEnd(decThisYear)
            .build()
        return MaterialDatePicker.Builder.datePicker()
            .setTitleText("Seleccione una fecha")
            .setCalendarConstraints(constraintsBuilder)
            .build()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
