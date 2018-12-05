package ariemaywibowo.footballwatcher.Adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import ariemaywibowo.footballwatcher.Models.PlayerItem
import ariemaywibowo.footballwatcher.Models.TeamsItem
import ariemaywibowo.footballwatcher.R
import ariemaywibowo.footballwatcher.R.id.*
import com.squareup.picasso.Picasso
import org.jetbrains.anko.find

class PlayerAdapter (private val context: Context?,
                       private var players: List<PlayerItem>,
                       private val listener: (PlayerItem) -> Unit) : RecyclerView.Adapter<PlayersViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PlayersViewHolder {
        return PlayersViewHolder(LayoutInflater.from(context).inflate(R.layout.players_view, p0, false))
    }

    override fun getItemCount(): Int = players.size

    override fun onBindViewHolder(holder: PlayersViewHolder, position: Int) {
        holder.bindItem(players[position], listener)
    }

}

class PlayersViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    private val playerName : TextView = view.find(R.id.playerName)
    private val playerPosition : TextView = view.find(R.id.playerPosition)
    private val playerPic : ImageView = view.find(R.id.playerPic)

    fun bindItem(players: PlayerItem, listener: (PlayerItem) -> Unit) {
        Picasso.get().load(players.strCutout).fit().into(playerPic)
        playerName.text = players.strPlayer
        playerPosition.text = players.strPosition
        itemView.setOnClickListener {
            listener(players)
        }
    }


}
