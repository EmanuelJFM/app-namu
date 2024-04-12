package com.example.namumovil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.namumovil.data.Repo
import com.example.namumovil.model.Reserva

class ReservaViewModel(): ViewModel()  {
    private val repo = Repo()

    fun getReservaData(): LiveData<MutableList<Reserva>> {
        return repo.getReservaData()
    }

    fun updateReservaEstado(reservaId: String, newEstado: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        repo.updateReservaEstado(reservaId, newEstado, onSuccess, onFailure)
    }
}
