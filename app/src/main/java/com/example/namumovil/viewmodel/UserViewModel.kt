package com.example.namumovil.viewmodel

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.namumovil.data.Repo
import com.example.namumovil.model.User
import com.google.firebase.storage.FirebaseStorage

class UserViewModel(): ViewModel() {
    private val repo = Repo()
    private val _photoUrls = MutableLiveData<List<String>>()
    private val storage = FirebaseStorage.getInstance()
    val photoUrls: LiveData<List<String>> get() = _photoUrls

    fun getCurrentUserData(): LiveData<User> {
        return repo.getCurrentUserData()
    }
    fun updateUserData(newName: String, newCell: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        repo.updateUserData(newName, newCell, onSuccess, onFailure)
    }

    fun descargarPdfYMostrarChooser(assetName: String, context: Context) {
        repo.downloadPdfFromFirebaseStorage(assetName, context) { file ->
            // Crea un intent para abrir el archivo PDF
            val intent = Intent(Intent.ACTION_VIEW)
            val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
            intent.setDataAndType(uri, "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            // Crea un chooser para que el usuario pueda elegir la aplicación con la que abrir el archivo PDF
            val chooser = Intent.createChooser(intent, "Elige una aplicación para abrir el PDF")
            // Inicia el chooser
            context.startActivity(chooser)
        }
    }
}
