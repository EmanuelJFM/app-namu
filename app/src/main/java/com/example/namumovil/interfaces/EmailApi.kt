package com.example.namumovil.interfaces


import com.example.namumovil.model.Request
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EmailApi {
    @POST("/sendEmail")
    fun sendEmail(@Body emailRequest: Request): Call<Void>
}
