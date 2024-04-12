package com.example.namumovil.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.namumovil.model.Reserva
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class Repo {
    private val reservaCollection: CollectionReference = FirebaseFirestore.getInstance().collection("Reservas")
    private val mutableData = MutableLiveData<MutableList<Reserva>>()
    private var ultimoNumeroTicket: Int = 1000

    fun saveReserva(reserva: Reserva, onSuccess: () -> Unit, onFailure: () -> Unit) {

        ultimoNumeroTicket++
        val numeroTicket = ultimoNumeroTicket
        val estado = "Pendiente"

        reserva.ticket = numeroTicket
        reserva.estado = estado

        reservaCollection.add(reserva)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure()
            }
    }



    fun getReservaData(): LiveData<MutableList<Reserva>> {
            reservaCollection.get().addOnSuccessListener { result ->
            val listData = mutableListOf<Reserva>()
            for (document in result){
                val id = document.id
                val nombres = document.getString("nombres")
                val telefono = document.getString("telefono")
                val cantPersonas = document.getString("cantidadPersonas")
                val fechaReserva = document.getString("fechaReserva")
                val horario = document.getString("horario")
                val comentarios = document.getString("comentarios")
                val ticketDouble = document.getDouble("ticket")
                val ticket = ticketDouble?.toInt()
                val estado = document.getString("estado")
                val reserva = Reserva(nombres!!, telefono!!,
                    cantPersonas!!, fechaReserva!!, horario!!, comentarios!!, ticket!!, estado!!, id!!)
                listData.add(reserva)
            }
            mutableData.value = listData
        }
        return mutableData
    }

    fun updateReservaEstado(reservaId: String, newEstado: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val reservaRef = reservaCollection.document(reservaId)

        val dataToUpdate = hashMapOf<String, Any>(
            "estado" to newEstado
        )

        reservaRef.update(dataToUpdate)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener {
                onFailure()
            }
    }


}