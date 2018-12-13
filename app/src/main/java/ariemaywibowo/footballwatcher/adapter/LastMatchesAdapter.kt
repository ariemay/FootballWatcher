package ariemaywibowo.footballwatcher.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ariemaywibowo.footballwatcher.models.EventsItem
import ariemaywibowo.footballwatcher.R
import ariemaywibowo.footballwatcher.R.id.*
import ariemaywibowo.footballwatcher.utility.Date.baseTime
import ariemaywibowo.footballwatcher.utility.Date.changeDate
import ariemaywibowo.footballwatcher.utility.Date.toGMTformat
import org.jetbrains.anko.find

class LastMatchesAdapter(private val context: Context?,
                         private var events: List<EventsItem>,
                         private val listener: (EventsItem) -> Unit) : RecyclerView.Adapter<MatchesViewHolder>() {

    lateinit var newEvents : MutableList<EventsItem>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MatchesViewHolder {
        return MatchesViewHolder(LayoutInflater.from(context).inflate(R.layout.match_view, parent, false))
    }

    override fun onBindViewHolder(holder: MatchesViewHolder, position: Int) {
        Log.i("LASTADAPTER", events.toString())
        holder.bindItem(events[position], listener)
    }

    override fun getItemCount(): Int = events.size
}


class MatchesViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    private val homeName : TextView = view.find(homeTeamName)
    private val awayName : TextView = view.find(awayTeamName)
    private val homeScore : TextView = view.find(homeTeamScore)
    private val awayScore : TextView = view.find(awayTeamScore)
    private val date : TextView = view.find(dateEvent)
    private val time : TextView = view.find(timeEvent)

    fun bindItem(events: EventsItem, listener: (EventsItem) -> Unit) {
        Log.i("LASTADAPTER11", events.toString())
        homeName.text = events.strHomeTeam
        awayName.text = events.strAwayTeam
        date.text = changeDate(events.dateEvent)
        if (events.intHomeScore != "null") {
            homeScore.text = events.intHomeScore
            awayScore.text = events.intAwayScore
        } else {
            homeScore.text = "-"
            awayScore.text = "-"
        }
        if (events.strTime.equals("00:00:00+00:00")) {
            time.text = "-"
        } else {
            Log.i("TIME", events.strTime.toString())
            val dateZero = baseTime(toGMTformat(events.dateEvent.toString(), events.strTime.toString())) + " WIB"
            time.text = dateZero

        }
        itemView.setOnClickListener {
            listener(events)
        }
    }
}