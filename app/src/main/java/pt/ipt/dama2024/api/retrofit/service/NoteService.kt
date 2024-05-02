package pt.ipt.dama2024.api.retrofit.service

import pt.ipt.dama2024.api.model.Note
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * description of function that request data from API
 *
 * @constructor Create empty Note service
 */
interface NoteService {

    /**
     * Get the Notes from API
     *
     * @return
     */
    @GET("api/notes") // defines which endpoint to use from API
    fun getNotes(): Call<List<Note>>

    /**
     * 'function' to add a note to API
     *
     * @param note
     */
    @POST("api/notes")
    fun addNote(@Body note:Note):Call<Note>
}