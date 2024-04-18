package pt.ipt.dama2024.api.model

import com.google.gson.annotations.SerializedName

/**
 * description of a Note that our API will send
 *
 * @property title title of Note
 * @property description content of Note
 * @constructor Create empty Note
 */
data class Note(
  @SerializedName("title")  val title:String,
  @SerializedName("description")  val description:String
)

// the use of @SerializedName("attribute name") allow us
// to use different attributes names in API and Model
// and connect them
