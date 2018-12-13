package ariemaywibowo.footballwatcher.presenter

import android.util.Log
import ariemaywibowo.footballwatcher.models.TeamsItem
import org.json.JSONObject

class TeamSearchPresenter {
    fun parsingSearch(response: String): ArrayList<TeamsItem> {
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
                dataList.add(
                    TeamsItem(
                        i.toLong(), idTeam,
                        strTeam,
                        strTeamBadge
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