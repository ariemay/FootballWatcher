package ariemaywibowo.footballwatcher.presenter

import android.content.Context
import android.util.Log
import ariemaywibowo.footballwatcher.helper.database
import ariemaywibowo.footballwatcher.models.EventsItem
import ariemaywibowo.footballwatcher.server.RetroService
import ariemaywibowo.footballwatcher.server.ServUtils
import ariemaywibowo.footballwatcher.utility.OutputServerStats
import ariemaywibowo.footballwatcher.view.MatchActivity
import okhttp3.ResponseBody
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MatchPresenter (private val view: MatchActivity) {

    fun getFav(context: Context?): ArrayList<EventsItem> {
        view.showLoading()
        val data: ArrayList<EventsItem> = ArrayList()

        context?.database?.use {
            Log.i("DATABASE", "Start?")
            val favorites = select(EventsItem.TABLE_FAVORITES)
                    .parseList(classParser<EventsItem>())
            Log.i("DATABASE", "Parsed")
            data.addAll(favorites)
        }
        view.hideLoading()
        return data
    }

    fun getLastMatches(leagueID: String?, callback: OutputServerStats) {
        view.showLoading()
        val newRetroServ: RetroService? = ServUtils.apiService
        if (leagueID != null) {
            newRetroServ?.getPastMatches(leagueID)?.enqueue(object: Callback<ResponseBody> {
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

    fun getNextMatch(leagueID: String?, callback: OutputServerStats) {
        view.showLoading()
        val newRetroServ: RetroService? = ServUtils.apiService
        if (leagueID != null) {
            newRetroServ?.getNextMatches(leagueID)?.enqueue(object: Callback<ResponseBody> {
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

    fun searchEvents(filterer: String?, callback: OutputServerStats) {
        view.showLoading()
        val newRetroServ: RetroService? = ServUtils.apiService
        if (filterer != null) {
            newRetroServ?.searchEvents(filterer)?.enqueue(object: Callback<ResponseBody> {
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

    fun parsingData(response: String): ArrayList<EventsItem> {
        Log.i("RESPONSE IN PARSING", response)
        val dataList: ArrayList<EventsItem> = ArrayList()
        try {
            val jsonObject = JSONObject(response)
            Log.i("JSON", jsonObject.toString())
            val message = jsonObject.getJSONArray("events")
            Log.i("JSONARRAY", message.length().toString())
            for (i in 0 until message.length()) {
                val data = message.getJSONObject(i)
                val dateEvent = data.getString("dateEvent")
                val timeEvent = data.getString("strTime")
                val idAwayTeam = data.getString("idAwayTeam")
                val idEvent = data.getString("idEvent")
                val idHomeTeam = data.getString("idHomeTeam")
                val idLeague = data.getString("idLeague")
                val intAwayScore = data.getString("intAwayScore")
                val intHomeScore = data.getString("intHomeScore")
                val strAwayTeam = data.getString("strAwayTeam")
                val strHomeTeam = data.getString("strHomeTeam")
                dataList.add(
                        EventsItem(
                                i.toLong(),
                                dateEvent,
                                timeEvent,
                                idAwayTeam,
                                idEvent,
                                idHomeTeam,
                                idLeague,
                                intAwayScore,
                                intHomeScore,
                                strAwayTeam,
                                strHomeTeam
                        )
                )
                Log.i("DATALIST", dataList.toString())
            }
        } catch (e: Exception) {
            Log.d("TAG", "Response error exception $e")
        }
        return dataList
    }
}