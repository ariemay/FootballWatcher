package ariemaywibowo.footballwatcher.Models

import com.google.gson.annotations.SerializedName


data class LeaguesItem(

	@SerializedName("strLeague")
	val strLeague: String? = null,

	@SerializedName("idLeague")
	val idLeague: String? = null
)