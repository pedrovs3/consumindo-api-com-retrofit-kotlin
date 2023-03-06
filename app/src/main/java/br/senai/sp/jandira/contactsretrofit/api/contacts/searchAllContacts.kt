package br.senai.sp.jandira.contactsretrofit.api.contacts

import br.senai.sp.jandira.contactsretrofit.models.Contact
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

fun searchAllContacts(onComplete: (List<Contact>) -> Unit) {
    val call = RetrofitApi.retrofitService().getAllUsers()

    call.enqueue(object :Callback<List<Contact>> {
        override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
            onComplete.invoke(response.body()!!)
        }

        override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
            TODO("Not yet implemented")
        }

    })
}