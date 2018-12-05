package ariemaywibowo.footballwatcher.Models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeamsItem(
	val id: Long? = null,
	val idTeam: String? = null,
	val strTeam: String? = null,
	val strTeamBadge: String? = null
) : Parcelable {
	companion object {
		const val TABLE_TEAM_FAV = "TABLE_TEAM_FAV"
		const val IDLONG = "IDLONG"
		const val ID_TEAM = "ID_TEAM"
		const val STR_TEAM_BADGE = "STR_TEAM_BADGE"
		const val STR_TEAM = "STR_TEAM"
	}
}
