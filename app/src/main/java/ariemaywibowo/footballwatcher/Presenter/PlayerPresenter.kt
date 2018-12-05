package ariemaywibowo.footballwatcher.Presenter

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import ariemaywibowo.footballwatcher.Helper.database
import ariemaywibowo.footballwatcher.Models.PlayerItem
import ariemaywibowo.footballwatcher.Models.TeamsItem
import ariemaywibowo.footballwatcher.Server.RetroService
import ariemaywibowo.footballwatcher.Server.ServUtils
import ariemaywibowo.footballwatcher.Utility.OutputServerStats
import ariemaywibowo.footballwatcher.View.Players.PlayerActivity
import okhttp3.ResponseBody
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.delete
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlayerPresenter (private val view: PlayerActivity) {

    fun getPlayerList (teamID : String?, callback: OutputServerStats) {
        view.showLoading()
        val newRetroServ: RetroService? = ServUtils.apiService
        if (teamID != null) {
            newRetroServ?.getPlayers(teamID)?.enqueue(object: Callback<ResponseBody> {
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

    fun isFavorite(context: Context, data: TeamsItem): Boolean {
        var bFavorite = false

        context.database.use {
            val favorites = select(TeamsItem.TABLE_TEAM_FAV)
                    .whereArgs(TeamsItem.ID_TEAM + " = {id}",
                            "id" to data.idTeam.toString())
                    .parseList(classParser<TeamsItem>())

            bFavorite = !favorites.isEmpty()
        }

        return bFavorite
    }

    fun removeFav(context: Context, data: TeamsItem) {
        try {
            context.database.use {
                delete(TeamsItem.TABLE_TEAM_FAV,
                    TeamsItem.IDLONG+ " = {id}",
                    "id" to data.idTeam.toString())
            }
        } catch (e: SQLiteConstraintException) {
            Log.i("RemovingError", e.toString())
        }
    }

    fun parsingPlayers(response: String): ArrayList<PlayerItem> {
        Log.i("RESPONSE IN PARSING", response)
        val dataList: ArrayList<PlayerItem> = ArrayList()
        try {
            val jsonObject = JSONObject(response)
            Log.i("JSON", jsonObject.toString())
            val message = jsonObject.getJSONArray("player")
            Log.i("JSONARRAY", message.length().toString())
            for (i in 0 until message.length()) {
                val data = message.getJSONObject(i)
                val idPlayer = data.getString("idPlayer")
                val strPlayer= data.getString("strPlayer")
                val strCutout = data.getString("strCutout")
                val strPosition = data.getString("strPosition")
                dataList.add(
                    PlayerItem(
                        i.toLong(), idPlayer,
                        strPlayer,
                        strCutout,
                        strPosition
                    )
                )

            }
        } catch (e: Exception) {
            Log.d("TAG", "Response error exception $e")
        }
        Log.i("PLAYERPRESENTER", dataList.toString())
        return dataList
    }

    fun addFav(context: Context, data: TeamsItem) {
        try {
            context.database.use {
                insert(TeamsItem.TABLE_TEAM_FAV,
                    TeamsItem.IDLONG to data.id,
                    TeamsItem.ID_TEAM to data.idTeam,
                    TeamsItem.STR_TEAM to data.strTeam,
                    TeamsItem.STR_TEAM_BADGE to data.strTeamBadge
                )
            }
        } catch (e: SQLiteConstraintException) {
            context.toast("Error: ${e.message}")
        }
    }

}