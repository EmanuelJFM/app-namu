package com.example.namumovil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.namumovil.data.Repo
import com.example.namumovil.model.Reserva
import com.example.namumovil.model.User

class ReservaViewModel(): ViewModel()  {
    private val repo = Repo()

    fun getReservaData(loggedInUserId: String): LiveData<MutableList<Reserva>> {
        return repo.getReservaData(loggedInUserId)
    }

    fun updateReservaEstado(reservaId: String, newEstado: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        repo.updateReservaEstado(reservaId, newEstado, onSuccess, onFailure)
    }

    fun updateReservaUser(reservaId: String, newReserva: Reserva) {
        repo.updateReservaUser(reservaId, newReserva)
    }
}
