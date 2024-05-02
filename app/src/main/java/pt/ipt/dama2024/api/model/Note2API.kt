package pt.ipt.dama2024.api.model

import com.google.gson.annotations.SerializedName

/**
 * new class to represent data to be sent to API
 *
 * @property title of note
 * @property description ot note
 * @constructor Create empty Note2a p i
 */
data class Note2API(
    @SerializedName("title")  val title:String,
    @SerializedName("description")  val description:String
)
