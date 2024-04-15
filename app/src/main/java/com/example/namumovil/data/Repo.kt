package com.example.namumovil.data

import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.namumovil.model.Reserva
import com.example.namumovil.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import java.io.File

class Repo {
    private val reservaCollection: CollectionReference = FirebaseFirestore.getInstance().collection("Reservas")
    private val userCollection: CollectionReference = FirebaseFirestore.getInstance().collection("users")
    private val storage = FirebaseStorage.getInstance()
    private val mutableData = MutableLiveData<MutableList<Reserva>>()
    private var ultimoNumeroTicket: Int = 1000

    fun getCurrentUserData(): LiveData<User> {
        val mutableData = MutableLiveData<User>()
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            userCollection.document(userId).get().addOnSuccessListener { document ->
                val name = document.getString("name") ?: ""
                val telefono = document.getString("phone") ?: ""
                val correo = document.getString("email") ?: ""

                val user = User(name!!, telefono!!, correo!!)
                mutableData.value = user
            }
        }

        return mutableData
    }

    fun saveReserva(reserva: Reserva, onSuccess: () -> Unit, onFailure: () -> Unit) {
        ultimoNumeroTicket++
        val numeroTicket = ultimoNumeroTicket
        val estado = "Pendiente"
        reserva.ticket = numeroTicket
        reserva.estado = estado
        val newDocRef = reservaCollection.document()
        reserva.reservaId = newDocRef.id
        newDocRef.set(reserva)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure()
            }
    }

    fun getReservaData(loggedInUserId: String): LiveData<MutableList<Reserva>> {
        reservaCollection.get().addOnSuccessListener { result ->
            val listData = mutableListOf<Reserva>()
            for (document in result){
                val userReservaId = document.getString("userId")
                if (userReservaId == loggedInUserId) {
                    val nombres = document.getString("nombres")
                    val telefono = document.getString("telefono")
                    val cantPersonas = document.getString("cantidadPersonas")
                    val fechaReserva = document.getString("fechaReserva")
                    val horario = document.getString("horario")
                    val comentarios = document.getString("comentarios")
                    val ticketDouble = document.getDouble("ticket")
                    val ticket = ticketDouble?.toInt()
                    val estado = document.getString("estado")
                    val userId = document.getString("userId")
                    val reservaId = document.id
                    val reserva = Reserva(nombres!!, telefono!!, cantPersonas!!, fechaReserva!!, horario!!, comentarios!!, ticket!!, estado!!,
                        userId!!, reservaId!!)
                    listData.add(reserva)
                }
            }
            mutableData.value = listData
        }
        return mutableData
    }

        fun updateReservaUser(reservaId: String, newReserva: Reserva) {
            val reservaRef = reservaCollection.document(reservaId)
            // Crear un nuevo mapa con los campos que quieres actualizar
            val updates = hashMapOf<String, Any>(
                "telefono" to newReserva.telefono,
                "cantidadPersonas" to newReserva.cantidadPersonas,
                "fechaReserva" to newReserva.fechaReserva,
                "horario" to newReserva.horario,
                "comentarios" to newReserva.comentarios
            )
            // Actualizar el documento en Firestore
            reservaRef.update(updates)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully updated!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error updating document", e) }
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

    fun downloadPdfFromFirebaseStorage(assetName: String, context: Context, onDownloadComplete: (File) -> Unit) {
        val storageRef = storage.reference
        val pdfRef = storageRef.child("pdf/$assetName")
        val tempFile = File.createTempFile("temp", null, context.cacheDir)
        tempFile.deleteOnExit()
        pdfRef.getFile(tempFile).addOnSuccessListener {
            Toast.makeText(context, "El archivo PDF se descargó correctamente", Toast.LENGTH_SHORT).show()
            onDownloadComplete(tempFile)
        }.addOnFailureListener {
            throw it
        }
    }

}

