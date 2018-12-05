package ariemaywibowo.footballwatcher.Server

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetroService {
    @GET(FootballAPI.eventsPastleague)
    fun getPastMatches(@Query("id") leagueID: String) : Call<ResponseBody>

    @GET(FootballAPI.eventsNextleague)
    fun getNextMatches(@Query("id") leagueID: String) : Call<ResponseBody>

    @GET(FootballAPI.lookupEvent)
    fun getEventDetail(@Query("id") eventID: String) : Call<ResponseBody>

    @GET(FootballAPI.lookupteam)
    fun getTeamBadge(@Query("id") eventID: String) : Call<ResponseBody>

    @GET(FootballAPI.searchevents)
    fun searchEvents(@Query("e") filterer: String) : Call<ResponseBody>

    @GET(FootballAPI.lookupallteam)
    fun getTeamList(@Query("id") leagueID: String) : Call<ResponseBody>

    @GET(FootballAPI.lookup_all_players)
    fun getPlayers(@Query("id") teamID: String) : Call<ResponseBody>

    @GET(FootballAPI.lookupplayer)
    fun getDetailOnPlayer(@Query("id") playerID: String) : Call<ResponseBody>

    @GET(FootballAPI.searchteams)
    fun searchTeam(@Query("t") playerID: String) : Call<ResponseBody>
}