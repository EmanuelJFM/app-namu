package com.example.namumovil.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.namumovil.R
import com.example.namumovil.data.Repo
import com.example.namumovil.databinding.FragmentCreateReservationBinding
import com.example.namumovil.model.Reserva
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.*



class CreateReservationFragment : Fragment() {
    private var _binding: FragmentCreateReservationBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateReservationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // DatePicker
        val datePicker = createDatePicker()
        binding.etDate.setOnClickListener {
            datePicker.show(childFragmentManager, "datePicker")
        }
        datePicker.addOnPositiveButtonClickListener {
            // Convertir el timestamp a una fecha legible
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val date = sdf.format(Date(it))
            binding.etDate.setText(date)
        }

        // TimePicker
        val timePicker = createTimePicker()
        binding.etTime.setOnClickListener {
            timePicker.show(childFragmentManager, "timePicker")
        }
        timePicker.addOnPositiveButtonClickListener {
            // Convertir la hora y los minutos a un formato legible
            val time = String.format("%02d:%02d", timePicker.hour, timePicker.minute)
            binding.etTime.setText(time)
        }
        binding.btnRegister.setOnClickListener {
            // Validar que todos los campos estén llenos
            if (binding.etName.text.isNullOrEmpty() ||
                binding.etPhone.text.isNullOrEmpty() ||
                binding.etNumPeople.text.isNullOrEmpty() ||
                binding.etDate.text.isNullOrEmpty() ||
                binding.etTime.text.isNullOrEmpty()
            ) {
                Toast.makeText(context, "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val repo = Repo()
            repo.saveReserva(
                Reserva(
                    binding.etName.text.toString(),
                    binding.etPhone.text.toString(),
                    binding.etNumPeople.text.toString(),
                    binding.etDate.text.toString(),
                    binding.etTime.text.toString(),
                    binding.etComments.text.toString()
                ),
                {
                    context?.let { it1 ->
                        MaterialAlertDialogBuilder(it1)
                            .setTitle(resources.getString(R.string.title_alert_dialog_reserva))
                            .setMessage(resources.getString(R.string.message_dialog_reserva))
                            .setIcon(resources.getDrawable(R.drawable.baseline_check_circle_24))
                            .setPositiveButton(resources.getString(R.string.accept)) { dialog, which ->
                                binding.etName.text?.clear()
                                binding.etPhone.text?.clear()
                                binding.etNumPeople.text?.clear()
                                binding.etDate.text?.clear()
                                binding.etTime.text?.clear()
                                binding.etComments.text?.clear()
                            }
                            .show()
                    }
                },
                {
                    Toast.makeText(context, "Error guardando el registro", Toast.LENGTH_SHORT).show()
                }
            )
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

    private fun createTimePicker(): MaterialTimePicker {
        return MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_12H)
            .setHour(12)
            .setMinute(10)
            .setTitleText("Seleccione una hora")
            .build()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}