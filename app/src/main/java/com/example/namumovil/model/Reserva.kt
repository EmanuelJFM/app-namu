package com.example.namumovil.model

import java.io.Serializable

data class Reserva(
    val nombres:String = "",
    val telefono:String = "",
    val cantidadPersonas: String = "",
    val fechaReserva: String = "",
    val horario:String = "",
    val comentarios:String = "",
    var ticket: Int = 0,
    var estado: String = "Pendiente", //
    var id: String =""
): Serializable {

}