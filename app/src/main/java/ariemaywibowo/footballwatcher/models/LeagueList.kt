package ariemaywibowo.footballwatcher.models

import com.google.gson.annotations.SerializedName

data class LeagueList(

	@SerializedName("leagues")
	val leagues: List<LeaguesItem>
)