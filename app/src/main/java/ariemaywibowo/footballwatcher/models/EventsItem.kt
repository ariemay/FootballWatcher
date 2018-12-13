package ariemaywibowo.footballwatcher.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventsItem(
    val id: Long?,
    val dateEvent: String?,
    val strTime: String?,
    val idAwayTeam: String?,
    val idEvent: String?,
    val idHomeTeam: String?,
    val idLeague: String?,
    val intAwayScore: String? = "0",
    val intHomeScore: String? = "0",
    val strAwayTeam: String?,
    val strHomeTeam: String?
) : Parcelable {

    companion object {
        const val TABLE_FAVORITES = "TABLE_FAVORITES"
        const val ID = "ID"
        const val DATE_EVENT = "DATE_EVENT"
        const val TIME_EVENT = "TIME_EVENT"
        const val ID_AWAY_TEAM = "ID_AWAY_TEAM"
        const val ID_EVENT = "ID_EVENT"
        const val ID_HOME_TEAM = "ID_HOME_TEAM"
        const val ID_LEAGUE = "ID_LEAGUE"
        const val INT_AWAY_SCORE = "INT_AWAY_SCORE"
        const val INT_HOME_SCORE = "INT_HOME_SCORE"
        const val STR_AWAY_TEAM = "STR_AWAY_TEAM"
        const val STR_HOME_TEAM = "STR_HOME_TEAM"
    }
}