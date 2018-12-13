package ariemaywibowo.footballwatcher.view.players

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import ariemaywibowo.footballwatcher.adapter.PlayerAdapter
import ariemaywibowo.footballwatcher.models.PlayerItem
import ariemaywibowo.footballwatcher.models.TeamsItem
import ariemaywibowo.footballwatcher.presenter.PlayerPresenter
import ariemaywibowo.footballwatcher.R
import ariemaywibowo.footballwatcher.utility.OutputServerStats
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_team_detail.*

class PlayerActivity: AppCompatActivity() {

    private var listPlayers = ArrayList<PlayerItem>()
    private var presenter = PlayerPresenter(this)
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var mData: TeamsItem


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_detail)

        val idTeam = intent.getStringExtra("ID_TEAM")
        val nameTeam = intent.getStringExtra("NAME_TEAM")
        mData = intent.getParcelableExtra("TO_FAV")
        teamName.text = nameTeam
        getAllPlayer(idTeam)

        Picasso.get().load(mData.strTeamFanart1).fit().centerCrop().into(imageTeam)
        teamName.text = mData.strTeam
        stadiumName.text = mData.strStadium
        coachName.text = mData.strManager

    }

    fun getAllPlayer (idTeam : String) {
        var data: MutableList<PlayerItem>
        presenter.getPlayerList(idTeam, object : OutputServerStats {

            override fun onSuccess(response: String) {
                Log.i("RESPONSE", response)
                try {
                    data = presenter.parsingPlayers(response)
                    if (data.size < 1) {
                        Toast.makeText(this@PlayerActivity, "Maaf, coba lagi", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.i("PLAYERLIST", data.toString())
                        playerList.setHasFixedSize(true)
                        playerList.layoutManager = LinearLayoutManager(this@PlayerActivity, LinearLayoutManager.VERTICAL, false)
                        playerList.adapter = PlayerAdapter(this@PlayerActivity, data, { data -> toDetailPlayer(data) })
                    }

                } catch (e: NullPointerException) {
                    Log.i("ERROR", "NullPointerException")
                }
            }

            override fun onFailed(response: String) {
                Log.i("ERROR", response)
            }

            override fun onFailure(throwable: Throwable?) {
                Toast.makeText(this@PlayerActivity, "No connection?", Toast.LENGTH_SHORT).show()
            }
        })

        isFavorite = presenter.isFavorite(this, mData)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu, menu)
        menuItem = menu
        setFavorite()
        return super.onCreateOptionsMenu(menu)
    }

    private fun setFavorite() {
        Log.i("ISFAVORITE", isFavorite.toString())
        Log.i("MENUITEM", menuItem?.getItem(0).toString())
        if (isFavorite)
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_added_favorites)
        else
            menuItem?.getItem(0)?.icon = ContextCompat.getDrawable(this, R.drawable.ic_add_to_favorites)
    }

    private fun toDetailPlayer(players: PlayerItem) {
        val idPlayer = players.idPlayer.toString()
        val showDetailActivityIntent = Intent(this, DetailPlayerActivity::class.java)
        showDetailActivityIntent.putExtra("ID_PLAYER", idPlayer)
        startActivity(showDetailActivityIntent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else if (item.itemId == R.id.add_to_favorite) {
            if (isFavorite) {
                presenter.removeFav(this, mData)
                Log.i("TAG REMOVE", mData.toString())
            } else {
                presenter.addFav(this, mData)
                Log.i("TAG ADD", mData.toString())
            }
            isFavorite = !isFavorite
            setFavorite()
            return true
        }
        return false
    }

    override fun onBackPressed() {
        finish()
    }
}
