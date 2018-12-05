package ariemaywibowo.footballwatcher.View.Details

import android.app.Activity
import android.content.Context
import ariemaywibowo.footballwatcher.Models.EventsItem

interface DetailInterface {
    fun isSuccess(response: String): Boolean
    fun addFav(context: Context, data: EventsItem)
    fun removeFav(context: Context, data: EventsItem)
    fun isFavorite(context: Context, data: EventsItem): Boolean
}