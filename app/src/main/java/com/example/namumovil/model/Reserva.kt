package com.example.namumovil.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reserva(
    val nombres:String = "",
    val telefono:String = "",
    val cantidadPersonas: String = "",
    val fechaReserva: String = "",
    val horario:String = "",
    val comentarios:String = "",
    var ticket: Int = 0,
    var estado: String = "Pendiente",
    var userId: String ="",
    var reservaId:String =""
): Parcelable
