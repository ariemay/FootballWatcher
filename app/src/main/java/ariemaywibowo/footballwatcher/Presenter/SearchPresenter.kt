package ariemaywibowo.footballwatcher.Presenter

import android.util.Log
import ariemaywibowo.footballwatcher.Models.EventsItem
import org.json.JSONObject

class SearchPresenter {
    fun parsingSearch(response: String): ArrayList<EventsItem> {
        Log.i("RESPONSE IN PARSING", response)
        val dataList: ArrayList<EventsItem> = ArrayList()
        try {
            val jsonObject = JSONObject(response)
            Log.i("JSON", jsonObject.toString())
            val message = jsonObject.getJSONArray("event")
            Log.i("JSONARRAY", message.length().toString())
            for (i in 0 until message.length()) {
                val data = message.getJSONObject(i)
                val dateEvent = data.getString("dateEvent")
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
                        i.toLong(), dateEvent, idAwayTeam,
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