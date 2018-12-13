package ariemaywibowo.footballwatcher.view.details


import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import ariemaywibowo.footballwatcher.models.EventsItem
import ariemaywibowo.footballwatcher.presenter.DetailPresenter
import ariemaywibowo.footballwatcher.R
import ariemaywibowo.footballwatcher.R.id.add_to_favorite
import ariemaywibowo.footballwatcher.utility.OutputServerStats
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.json.JSONObject


class DetailActivity : AppCompatActivity() {

    private val presenter = DetailPresenter()
    private var badgeHome : String? = null
    private var badgeAway : String? = null
    private var menuItem: Menu? = null
    private var isFavorite: Boolean = false
    private lateinit var mData: EventsItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val homeName = HOME_NAME
        val homeScore = HOME_SCORE
        val awayName = AWAY_NAME
        val awayScore = AWAY_SCORE
        val homeScorer = HOME_SCORERS
        val awayScorer = AWAY_SCORERS
        val homeYellow = HOME_YELLOW
        val awayYellow = AWAY_YELLOW
        val homeRed = HOME_RED
        val awayRed = AWAY_RED
        val homeForward = HOME_FORWARD
        val awayForward = AWAY_FORWARD
        val homeMidfielder = HOME_MIDFIELDER
        val awayMidfielder = AWAY_MIDFIELDER
        val homeDefender = HOME_DEFENDER
        val awayDefender = AWAY_DEFENDER
        val homeKeeper = HOME_KEEPER
        val awayKeeper = AWAY_KEEPER

        val eventsId = intent.getStringExtra("ID_EVENTS")
        val passedItems = intent.getStringArrayListExtra("PASSEDITEM")
        mData = intent.getParcelableExtra("TOFAV")
        Log.i("TOFAV", mData.toString())


        getBadges(passedItems)

        presenter.getDetailMatch(this, eventsId, object: OutputServerStats {
            override fun onFailed(response: String) {
                Toast.makeText(this@DetailActivity, "Failed. Coba lagi", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(throwable: Throwable?) {
                Toast.makeText(this@DetailActivity, "Check your connection", Toast.LENGTH_SHORT).show()
            }

            override fun onSuccess(response: String) {
                Log.i("DETAILRESPONSE", response)
                val jsonObject = JSONObject(response)
                val event = jsonObject.getJSONArray("events")
                for( i in 0 until event.length()) {
                    val events = event.getJSONObject(i)
                    if (events.getString("intHomeScore") != "null") {
                        val homeNm = events.getString("strHomeTeam")
                        val homeSc = events.getString("intHomeScore")
                        val awayNm = events.getString("strAwayTeam")
                        val awaySc = events.getString("intAwayScore")
                        val homeScr = events.getString("strHomeGoalDetails")
                        val awayScr = events.getString("strAwayGoalDetails")
                        val homeYel = events.getString("strHomeYellowCards")
                        val awayYel = events.getString("strAwayYellowCards")
                        val hmRed = events.getString("strHomeRedCards")
                        val awRed = events.getString("strAwayRedCards")
                        val homeFwd = events.getString("strHomeLineupForward")
                        val awayFwd = events.getString("strAwayLineupForward")
                        val homeMid = events.getString("strHomeLineupMidfield")
                        val awayMid = events.getString("strAwayLineupMidfield")
                        val homeDev = events.getString("strHomeLineupDefense")
                        val awayDev = events.getString("strAwayLineupDefense")
                        val homeKep = events.getString("strHomeLineupGoalkeeper")
                        val awayKep = events.getString("strAwayLineupGoalkeeper")

                        if (homeNm.equals("null")) {
                            homeName.text = ""
                        } else {
                            homeName.text = homeNm
                        }
                        if (awayNm.equals("null")) {
                            awayName.text = ""
                        } else {
                            awayName.text = awayNm
                        }
                        if (homeSc.equals("null")) {
                            homeScore.text = ""
                        } else {
                            homeScore.text = homeSc
                        }
                        if (awaySc.equals("null")) {
                            awayScore.text = ""
                        } else {
                            awayScore.text = awaySc
                        }
                        if (homeScr.equals("null")) {
                            homeScorer.text = ""
                        } else {
                            homeScorer.text = homeScr
                        }
                        if (awayScr.equals("null")) {
                            awayScorer.text = ""
                        } else {
                            awayScorer.text = awayScr
                        }
                        if (homeYel.equals("null")) {
                            homeYellow.text = ""
                        } else {
                            homeYellow.text = homeYel
                        }
                        if (awayYel.equals("null")) {
                            awayYellow.text = ""
                        } else {
                            awayYellow.text = awayYel
                        }
                        if (hmRed.equals("null")) {
                            homeRed.text = ""
                        } else {
                            homeRed.text = hmRed
                        }
                        if (awRed.equals("null")) {
                            awayRed.text = ""
                        } else {
                            awayRed.text = awRed
                        }
                        if (homeFwd.equals("null")) {
                            homeForward.text = ""
                        } else {
                            homeForward.text = homeFwd
                        }
                        if (awayFwd.equals("null")) {
                            awayForward.text = ""
                        } else {
                            awayForward.text = awayFwd
                        }
                        if (homeMid.equals("null")) {
                            homeMidfielder.text = ""
                        } else {
                            homeMidfielder.text = homeMid
                        }
                        if (awayMid.equals("null")) {
                            awayMidfielder.text = ""
                        } else {
                            awayMidfielder.text = awayMid
                        }
                        if (homeDev.equals("null")) {
                            awayDefender.text = ""
                        } else {
                            awayDefender.text = homeDev
                        }
                        if (awayDev.equals("null")) {
                            awayDefender.text = ""
                        } else {
                            awayDefender.text = awayDev
                        }
                        if (homeKep.equals("null")) {
                            homeKeeper.text = ""
                        } else {
                            homeKeeper.text = homeKep
                        }
                        if (awayKep.equals("null")) {
                            awayKeeper.text = ""
                        } else {
                            awayKeeper.text = awayKep
                        }

//                    } else {
//                        homeName.text = events.getString("strHomeTeam")
//                        homeScore.text = "?"
//                        awayName.text = events.getString("strAwayTeam")
//                        awayScore.text = "?"
//                    }
                    }
                }
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

    private fun getBadges (passedItems: ArrayList<String>) {
        val homeBadge = BADGE_HOME
        val awayBadge = BADGE_AWAY
        for (i in 0 until passedItems.size) {
            Log.i("PASSEDDATA", passedItems.toString())
            presenter.getBadgeForDetail(this, passedItems[i], object : OutputServerStats {
                override fun onSuccess(response: String) {
                    if (i == 0) {
                        val jsonObject = JSONObject(response)
                        val teamsDet = jsonObject.getJSONArray("teams")
                        for (i in 0 until teamsDet.length()) {
                            val data = teamsDet.getJSONObject(i)
                            badgeHome = data.getString("strTeamBadge")
                        }
                    } else if (i == 1) {
                        val jsonObject = JSONObject(response)
                        Log.i("BADGES", jsonObject.toString())
                        val teamsDet = jsonObject.getJSONArray("teams")
                        for (i in 0 until teamsDet.length()) {
                            val data = teamsDet.getJSONObject(i)
                            badgeAway= data.getString("strTeamBadge")
                        }
                    }
                    Picasso.get().load(badgeHome).fit().centerInside().into(homeBadge)
                    Picasso.get().load(badgeAway).fit().centerInside().into(awayBadge)
                }

                override fun onFailed(response: String) {
                    Toast.makeText(this@DetailActivity, "Silahkan coba lagi", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(throwable: Throwable?) {
                    Toast.makeText(this@DetailActivity, "Coba cek koneksinya gimana...", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        } else if (item.itemId == add_to_favorite) {
            if (isFavorite) {
                presenter.removeFav(this, mData)
                Log.i("TAG REMOVE", "Remove fav")
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
