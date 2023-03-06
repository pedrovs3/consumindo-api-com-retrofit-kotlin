package br.senai.sp.jandira.contactsretrofit.api.contacts

import br.senai.sp.jandira.contactsretrofit.constants.Constants
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitApi {
    companion object {
        private lateinit var instance: Retrofit

        fun getRetrofit(): Retrofit {
            if (!Companion::instance.isInitialized) {
                instance = Retrofit
                    .Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                return instance
            }

            return instance
        }

        fun retrofitService(): ContactRetrofitService {
            return instance.create(ContactRetrofitService::class.java)
        }
    }
}