package ariemaywibowo.footballwatcher.presenter

import android.app.Activity
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import ariemaywibowo.footballwatcher.helper.database
import ariemaywibowo.footballwatcher.models.EventsItem
import ariemaywibowo.footballwatcher.server.RetroService
import ariemaywibowo.footballwatcher.server.ServUtils
import ariemaywibowo.footballwatcher.utility.OutputServerStats
import ariemaywibowo.footballwatcher.view.details.DetailInterface
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

class DetailPresenter : DetailInterface {
    override fun removeFav(context: Context, data: EventsItem) {
        try {
            context.database.use {
                delete(EventsItem.TABLE_FAVORITES,
                        EventsItem.ID_EVENT + " = {id}",
                        "id" to data.idEvent.toString())
            }
        } catch (e: SQLiteConstraintException) {
            Log.i("RemovingError", e.toString())
        }
    }

    override fun isSuccess(response: String): Boolean {
        var success = true
        try {
            val jObj = JSONObject(response)
            success = jObj.getBoolean("success")
        } catch (e: Exception) {
            Log.i("ERRORONSUCCESS", e.toString())
        }
        return success
    }

    override fun isFavorite(context: Context, data: EventsItem): Boolean {
        var bFavorite = false

        context.database.use {
            val favorites = select(EventsItem.TABLE_FAVORITES)
                    .whereArgs(EventsItem.ID_EVENT + " = {id}",
                            "id" to data.idEvent.toString())
                    .parseList(classParser<EventsItem>())

            bFavorite = !favorites.isEmpty()
        }

        return bFavorite
    }

    fun getDetailMatch(context: Activity, eventID: String?, callback: OutputServerStats) {
        val newRetroServ: RetroService? = ServUtils.apiService
        if (eventID != null) {
            newRetroServ?.getEventDetail(eventID)?.enqueue(object: Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    callback.onFailure(t)
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            callback.onSuccess(response.body().string())
                        } else {
                            callback.onFailed(response.errorBody().string())
                        }
                    }
                }
            })
        }
    }
    fun getBadgeForDetail(context: Activity, teamID: String?, callback: OutputServerStats) {
        val newRetroServ: RetroService? = ServUtils.apiService
        if (teamID != null) {
            newRetroServ?.getTeamBadge(teamID)?.enqueue(object: Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>?, t: Throwable?) {
                    callback.onFailure(t)
                }

                override fun onResponse(call: Call<ResponseBody>?, response: Response<ResponseBody>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            callback.onSuccess(response.body().string())
                        } else {
                            callback.onFailed(response.errorBody().string())
                        }
                    }
                }
            })
        }
    }
    override fun addFav(context: Context, data: EventsItem) {
        try {
            context.database.use {
                insert(EventsItem.TABLE_FAVORITES,
                        EventsItem.ID to data.id,
                        EventsItem.DATE_EVENT to data.dateEvent,
                        EventsItem.TIME_EVENT to data.strTime,
                        EventsItem.ID_AWAY_TEAM to data.idAwayTeam,
                        EventsItem.ID_EVENT to data.idEvent,
                        EventsItem.ID_HOME_TEAM to data.idHomeTeam,
                        EventsItem.ID_LEAGUE to data.idLeague,
                        EventsItem.INT_AWAY_SCORE to data.intAwayScore,
                        EventsItem.INT_HOME_SCORE to data.intHomeScore,
                        EventsItem.STR_AWAY_TEAM to data.strAwayTeam,
                        EventsItem.STR_HOME_TEAM to data.strHomeTeam
                )
            }
        } catch (e: SQLiteConstraintException) {
            context.toast("Error: ${e.message}")
        }
    }
}