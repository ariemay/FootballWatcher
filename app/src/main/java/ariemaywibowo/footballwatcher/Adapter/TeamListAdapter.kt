package ariemaywibowo.footballwatcher.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ariemaywibowo.footballwatcher.Models.TeamsItem
import ariemaywibowo.footballwatcher.R
import ariemaywibowo.footballwatcher.R.id.*
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find

class TeamListAdapter (private val context: Context?,
                       private var teams: List<TeamsItem>,
                       private val listener: (TeamsItem) -> Unit) : RecyclerView.Adapter<TeamListViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): TeamListViewHolder {
        return TeamListViewHolder(LayoutInflater.from(context).inflate(R.layout.teams_view, p0, false))
    }

    override fun getItemCount(): Int = teams.size

    override fun onBindViewHolder(holder: TeamListViewHolder, position: Int) {
        holder.bindItem(teams[position], listener)
    }

}

class TeamListViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val teamName : TextView = view.find(R.id.teamName)
    private val teamBadge : ImageView = view.find(R.id.teamBadge)

    fun bindItem(teams: TeamsItem, listener: (TeamsItem) -> Unit) {
        Picasso.get().load(teams.strTeamBadge).fit().into(teamBadge)
        teamName.text = teams.strTeam
        itemView.setOnClickListener {
            listener(teams)
        }
    }


}
