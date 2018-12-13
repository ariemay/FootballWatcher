package ariemaywibowo.footballwatcher.presenter

import android.util.Log
import ariemaywibowo.footballwatcher.models.DetailPlayerItem
import ariemaywibowo.footballwatcher.server.RetroService
import ariemaywibowo.footballwatcher.server.ServUtils
import ariemaywibowo.footballwatcher.utility.OutputServerStats
import ariemaywibowo.footballwatcher.view.players.DetailPlayerActivity
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPlayerPresenter (private val view: DetailPlayerActivity){

    fun getDetailPlayer (playerID : String?, callback: OutputServerStats) {
        val newRetroServ: RetroService? = ServUtils.apiService
        if (playerID != null) {
            newRetroServ?.getDetailOnPlayer(playerID)?.enqueue(object: Callback<ResponseBody> {
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
    }

    fun parsingDetailPlayer(response: String): ArrayList<DetailPlayerItem> {
        Log.i("RESPONSE IN PARSING", response)
        val dataList: ArrayList<DetailPlayerItem> = ArrayList()
        try {
            val jsonObject = JSONObject(response)
            Log.i("JSON", jsonObject.toString())
            val message = jsonObject.getJSONArray("players")
            Log.i("JSONARRAY", message.length().toString())
            for (i in 0 until message.length()) {
                val data = message.getJSONObject(i)
                val strFanart1 = data.getString("strFanart1")
                val strPlayer= data.getString("strPlayer")
                val strWeight = data.getString("strWeight")
                val strHeight = data.getString("strHeight")
                val strDescriptionEN = data.getString("strDescriptionEN")
                dataList.add(
                        DetailPlayerItem(
                                i.toLong(), strFanart1,
                                strPlayer,
                                strWeight,
                                strHeight,
                                strDescriptionEN
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