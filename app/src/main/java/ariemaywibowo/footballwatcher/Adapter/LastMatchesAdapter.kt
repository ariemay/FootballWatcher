package ariemaywibowo.footballwatcher.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ariemaywibowo.footballwatcher.Models.EventsItem
import ariemaywibowo.footballwatcher.R
import ariemaywibowo.footballwatcher.R.id.*
import ariemaywibowo.footballwatcher.Utility.Date.changeDate
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
    private val HOME_TEAM_NAME : TextView = view.find(homeTeamName)
    private val AWAY_TEAM_NAME : TextView = view.find(awayTeamName)
    private val HOME_TEAM_SCORE : TextView = view.find(homeTeamScore)
    private val AWAY_TEAM_SCORE : TextView = view.find(awayTeamScore)
    private val DATE_EVENT : TextView = view.find(dateEvent)

    fun bindItem(events: EventsItem, listener: (EventsItem) -> Unit) {
        Log.i("LASTADAPTER11", events.toString())
        HOME_TEAM_NAME.text = events.strHomeTeam
        AWAY_TEAM_NAME.text = events.strAwayTeam
        DATE_EVENT.text = changeDate(events.dateEvent)
        if (events.intHomeScore != "null") {
            HOME_TEAM_SCORE.text = events.intHomeScore
            AWAY_TEAM_SCORE.text = events.intAwayScore
        } else {
            HOME_TEAM_SCORE.text = "?"
            AWAY_TEAM_SCORE.text = "?"
        }
        itemView.setOnClickListener {
            listener(events)
        }
    }
}