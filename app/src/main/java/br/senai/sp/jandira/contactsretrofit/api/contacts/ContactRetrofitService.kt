package br.senai.sp.jandira.contactsretrofit.api.contacts

import br.senai.sp.jandira.contactsretrofit.models.Contact
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ContactRetrofitService {

    @GET("contacts")
    fun getAllUsers(): Call<List<Contact>>

    @GET("contacts/{id}")
    fun getUserById(@Path("id") id: Number) : Call<Contact>

    @POST("contacts")
    fun createContact(@Body contact: Contact): Call<Contact>

    @DELETE("contacts/{id}")
    fun deleteContact(@Path("id") id: Long): Call<String>

    @PUT("contacts/{id}")
    fun updateUser(@Path("id") id: Number, @Body contact: Contact): Call<Contact>

}