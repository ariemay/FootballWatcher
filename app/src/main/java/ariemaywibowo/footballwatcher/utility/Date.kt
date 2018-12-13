package ariemaywibowo.footballwatcher.utility

import android.util.Log
import java.lang.Exception
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.Date

object Date {

    private fun formatDate(date: String, format: String): String {
        var result = ""
        val old = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

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

    fun toGMTformat(date: String, time: String?): Date? {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        return try {
            val dateTime = "$date $time"
            Log.i("DATEBASETIME", dateTime)
            formatter.parse(dateTime)
        } catch (e: Exception) {
            val dateTime = "$date 00:00:00+00:00"
            Log.i("timeException", dateTime)
            formatter.parse(dateTime)
        }

    }

    fun baseTime(date: Date?): String? {
        val formate = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formate.format(date)

    }

}