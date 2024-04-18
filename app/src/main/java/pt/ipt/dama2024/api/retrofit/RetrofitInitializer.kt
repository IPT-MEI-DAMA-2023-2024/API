package pt.ipt.dama2024.api.retrofit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import pt.ipt.dama2024.api.retrofit.service.NoteService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * this class is responsible for
 * comunitation with API
 */
class RetrofitInitializer {

    // this object will translate the JSON content to ANDROID app
    private val gson: Gson = GsonBuilder().setLenient().create()

    // location of our API
    private val host = "https://adamastor.ipt.pt/DAM-API/"

    // 'opens' the connection to API
    private val retrofit =
        Retrofit.Builder().baseUrl(host)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    fun noteService(): NoteService = retrofit.create(NoteService::class.java)


}