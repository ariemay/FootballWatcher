package ariemaywibowo.footballwatcher.view.details

import android.content.Context
import ariemaywibowo.footballwatcher.models.EventsItem

interface DetailInterface {
    fun isSuccess(response: String): Boolean
    fun addFav(context: Context, data: EventsItem)
    fun removeFav(context: Context, data: EventsItem)
    fun isFavorite(context: Context, data: EventsItem): Boolean
}