package ariemaywibowo.footballwatcher.Utility

import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Date {

    private fun formatDate(date: String, format: String): String {
        var result = ""
        val old = SimpleDateFormat("yyyy-MM-dd")

        try {
            val oldDate = old.parse(date)
            val newFormat = SimpleDateFormat(format)

            result = newFormat.format(oldDate)
        } catch (e: ParseException) {
            Log.i("ErrorInDate", e.toString())
        }

        return result
    }


    fun changeDate(date: String?): String {
        return formatDate(date.toString(), "EEE, dd MMM yyyy")
    }
}