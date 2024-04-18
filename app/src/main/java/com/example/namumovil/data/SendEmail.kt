import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.namumovil.interfaces.EmailApi
import com.example.namumovil.model.Request
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SendEmail(private val context: Context) {
    fun sendEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(user?.uid!!)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val email = document.getString("email")
                    sendEmailWithRetrofit(email)
                } else {
                    Log.d("Firestore", "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("Firestore", "get failed with ", exception)
            }
    }
    private fun sendEmailWithRetrofit(email: String?) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-mensajeria.onrender.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val emailApi = retrofit.create(EmailApi::class.java)
        val emailRequest = Request(
            email.toString(),"SU RESERVA HA SIDO GENERADA CON EXITO","Su reserva ha sido guardada con exito\n" +
                "Para dudas o consultas acerca de su reserva puede comunicarse con el numero +51 987654321")
        emailApi.sendEmail(emailRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(context,"Correo enviado con exito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,"Ha ocurrido un error", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e("Error", t.message ?: "Error desconocido")
            }
        })
    }
}
