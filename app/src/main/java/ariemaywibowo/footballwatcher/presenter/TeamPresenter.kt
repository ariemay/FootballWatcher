package ariemaywibowo.footballwatcher.presenter

import android.content.Context
import android.util.Log
import ariemaywibowo.footballwatcher.helper.database
import ariemaywibowo.footballwatcher.models.TeamsItem
import ariemaywibowo.footballwatcher.server.RetroService
import ariemaywibowo.footballwatcher.server.ServUtils
import ariemaywibowo.footballwatcher.utility.OutputServerStats
import ariemaywibowo.footballwatcher.view.teams.TeamListActivity
import okhttp3.ResponseBody
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TeamPresenter (private val view: TeamListActivity) {

    fun getFav(context: Context?): ArrayList<TeamsItem> {
        view.showLoading()
        val teamData: ArrayList<TeamsItem> = ArrayList()

        context?.database?.use {
            Log.i("DATABASE", "Start?")
            val teamFavorites = select(TeamsItem.TABLE_TEAM_FAV)
                    .parseList(classParser<TeamsItem>())
            Log.i("DATABASE", "Parsed")
            teamData.addAll(teamFavorites)
        }
        view.hideLoading()
        return teamData
    }

    fun getTeamList (leagueID : String?, callback: OutputServerStats) {
        view.showLoading()
        val newRetroServ: RetroService? = ServUtils.apiService
        if (leagueID != null) {
            newRetroServ?.getTeamList(leagueID)?.enqueue(object: Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    callback.onFailure(t)
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            callback.onSuccess(response.body().string())
                        } else {
                            callback.onFailed(response.errorBody().toString())
                        }
                    }
                }
            })
        }
        view.hideLoading()
    }

    fun parsingTeam(response: String): ArrayList<TeamsItem> {
        Log.i("RESPONSE IN PARSING", response)
        val dataList: ArrayList<TeamsItem> = ArrayList()
        try {
            val jsonObject = JSONObject(response)
            Log.i("JSON", jsonObject.toString())
            val message = jsonObject.getJSONArray("teams")
            Log.i("JSONARRAY", message.length().toString())
            for (i in 0 until message.length()) {
                val data = message.getJSONObject(i)
                val idTeam = data.getString("idTeam")
                val strTeam= data.getString("strTeam")
                val strTeamBadge = data.getString("strTeamBadge")
                val strStadium = data.getString("strStadium")
                val strManager = data.getString("strManager")
                val strFanart = data.getString("strTeamFanart1")
                dataList.add(
                        TeamsItem(
                                i.toLong(), idTeam,
                                strTeam,
                                strTeamBadge,
                                strStadium,
                                strManager,
                                strFanart
                        )
                )
                Log.i("DATALIST", dataList.toString())
            }
        } catch (e: Exception) {
            Log.d("TAG", "Response error exception $e")
        }
        return dataList
    }

    fun searchTeam(filterer: String?, callback: OutputServerStats) {
        view.showLoading()
        val newRetroServ: RetroService? = ServUtils.apiService
        if (filterer != null) {
            newRetroServ?.searchTeam(filterer)?.enqueue(object: Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    callback.onFailure(t)
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            callback.onSuccess(response.body().string())
                        } else {
                            callback.onFailed(response.errorBody().toString())
                        }
                    }
                }
            })
        }
        view.hideLoading()
    }

}