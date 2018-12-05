package ariemaywibowo.footballwatcher.Models

import com.google.gson.annotations.SerializedName

data class LeagueList(

	@SerializedName("leagues")
	val leagues: List<LeaguesItem>
)