package ariemaywibowo.footballwatcher.View.Teams

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.support.v7.widget.SearchView
import android.widget.Toast
import ariemaywibowo.footballwatcher.Adapter.TeamListAdapter
import ariemaywibowo.footballwatcher.MatchesFragments.MainView
import ariemaywibowo.footballwatcher.Models.LeaguesItem
import ariemaywibowo.footballwatcher.Models.TeamsItem
import ariemaywibowo.footballwatcher.Presenter.TeamPresenter
import ariemaywibowo.footballwatcher.Presenter.TeamSearchPresenter
import ariemaywibowo.footballwatcher.R
import ariemaywibowo.footballwatcher.Utility.OutputServerStats
import ariemaywibowo.footballwatcher.Utility.invisible
import ariemaywibowo.footballwatcher.Utility.visible
import ariemaywibowo.footballwatcher.View.MatchActivity
import ariemaywibowo.footballwatcher.View.Players.PlayerActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_match.*
import kotlinx.android.synthetic.main.activity_team_list.*
import org.json.JSONArray
import org.json.JSONObject

class TeamListActivity : AppCompatActivity(), MainView {

    private var listLeague = ArrayList<LeaguesItem>()
    private lateinit var spinnerID : String
    private var navMenu = 1
    private var presenter = TeamPresenter(this)
    private var teamSearchPresenter = TeamSearchPresenter()
    private lateinit var swipeRefresh : SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_team_list)

        allLeagues()
        callSpinner(navMenu)
        startTeamBottomNav()

        swipeRefresh = teamSwipe
        swipeRefresh.setOnRefreshListener {
            if (swipeRefresh.isRefreshing) {
                swipeRefresh.isRefreshing = false
                showLoading()
                callSpinner(navMenu)
                containerToShow(spinnerID, navMenu)
                hideLoading()
            }

        }
    }

    private fun allLeagues() {

        val queue = Volley.newRequestQueue(this)
        val url: String = "https://www.thesportsdb.com/api/v1/json/1/all_leagues.php"

        val stringReq = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->

                    var leagueList = response.toString()
                    val jsonObj : JSONObject = JSONObject(leagueList)
                    val jsonArray: JSONArray = jsonObj.getJSONArray("leagues")
                    val lengthResponse = jsonArray.length()
                    val idLeague : ArrayList<String> = ArrayList()
                    val strLeague : ArrayList<String> = ArrayList()
                    val soccerType = arrayOfNulls<String>(lengthResponse)
                    var counList = 0
                    Log.i("SPINNER1", listLeague.toString())
                    listLeague.clear()
                    if (lengthResponse > 0) {
                        for (i in 0 until lengthResponse) {
                            var jsonInner: JSONObject = jsonArray.getJSONObject(i)
                            Log.i("SPINNER2", jsonInner.toString())
                            soccerType[i] = jsonInner.getString("strSport")
                            if (soccerType[i] == "Soccer") {
                                idLeague.add(jsonInner.getString("idLeague"))
                                strLeague.add(jsonInner.getString("strLeague"))
                                listLeague.add(
                                        LeaguesItem(
                                                strLeague[counList],
                                                idLeague[counList]
                                        )
                                )
                                counList+=1
                            }
                        }
                    }
                    teamSpinner.adapter = ArrayAdapter<String>(this@TeamListActivity,
                            android.R.layout.simple_spinner_dropdown_item, strLeague)
                },
                Response.ErrorListener { Log.i("ERROR NIH", "league error") })
        queue.add(stringReq)
    }

    private fun callSpinner(navMenu: Int) {
        teamSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                spinnerID = listLeague[position].idLeague!!
                Log.i("idLeague", spinnerID)
                when (navMenu) {
                    1 -> containerToShow(spinnerID, 1)
                    2 -> containerToShow(spinnerID, 2)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }
    }

    private fun containerToShow(spinnerID: String, navMenu: Int) {
        var data: MutableList<TeamsItem>
        if (navMenu == 1) {
            showLoading()
            presenter.getTeamList(spinnerID, object : OutputServerStats {

                override fun onSuccess(response: String) {
                    Log.i("RESPONSE", response)
                    try {
                        data = presenter.parsingTeam(response)
                        if (data.size < 1) {
                            Toast.makeText(this@TeamListActivity, "Maaf, coba lagi", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.i("DATALAST1", data.toString())
                            teamList.setHasFixedSize(true)
                            teamList.layoutManager = LinearLayoutManager(this@TeamListActivity, LinearLayoutManager.VERTICAL, false)
                            teamList.adapter = TeamListAdapter(this@TeamListActivity, data, {data -> toDetailTeam(data)})
                        }

                    } catch (e: NullPointerException) {
                        Log.i("ERROR", "NullPointerException")
                    }
                }

                override fun onFailed(response: String) {
                    Log.i("ERROR", response)
                }

                override fun onFailure(throwable: Throwable?) {
                    Toast.makeText(this@TeamListActivity, "No connection?", Toast.LENGTH_SHORT).show()
                }
            })
        } else if (navMenu == 2) {
            val data : MutableList<TeamsItem>
            Log.i("DATAFAV", "start?")
            data = presenter.getFav(this@TeamListActivity)
            if (data.size < 1) {
                Toast.makeText(this@TeamListActivity, "No Data", Toast.LENGTH_SHORT).show()
            } else {
                teamList.layoutManager = LinearLayoutManager(this@TeamListActivity, LinearLayoutManager.VERTICAL, false)
                teamList.adapter = TeamListAdapter(this@TeamListActivity, data, { data -> toDetailTeam(data) })
            }
        }
    }

    private fun startTeamBottomNav() {
        navigationTeams.setOnNavigationItemSelectedListener(bottomNavigationListener)
    }

    private val bottomNavigationListener by lazy {
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.teams -> {
                    teamSpinner.visibility = View.VISIBLE
                    navMenu = 1
                    title = getString(R.string.Team)
                    callSpinner(navMenu)
                    containerToShow(spinnerID, navMenu)
                    true
                }
                R.id.favorites -> {
                    navMenu = 2
                    title = getString(R.string.favorites)
                    callSpinner(navMenu)
                    containerToShow("0", navMenu)
                    true
                }
                R.id.Matches -> {
                    navMenu = 3
                    title = getString(R.string.Matches)
                    backToMain()
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    private fun backToMain() {
        val showDetailActivityIntent = Intent(this, MatchActivity::class.java)
        startActivity(showDetailActivityIntent)
    }

    private fun toDetailTeam(teams: TeamsItem) {
        val idTeam = teams.idTeam.toString()
        val strTeam = teams.strTeam.toString()
        val toFav = teams
        val showDetailActivityIntent = Intent(this, PlayerActivity::class.java)
        showDetailActivityIntent.putExtra("ID_TEAM", idTeam)
        showDetailActivityIntent.putExtra("NAME_TEAM", strTeam)
        showDetailActivityIntent.putExtra("TO_FAV", toFav)
        startActivity(showDetailActivityIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = this@TeamListActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(this@TeamListActivity.componentName))
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Log.i("ONSUBMIT", p0)
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                Log.i("TEXTCHANGE", p0)
                if (p0 != null) {
                    var filteredData: MutableList<TeamsItem>
                    presenter.searchTeam(p0, object : OutputServerStats {

                        override fun onSuccess(response: String) {
                            Log.i("RESPONSE QUERY", response)
                            try {
                                filteredData = teamSearchPresenter.parsingSearch(response)
//                                var datas = presenter.parsingData(response)
                                Log.i("FILTERDATA", filteredData.toString())
                                if (filteredData.size < 1) {
                                    Toast.makeText(this@TeamListActivity, "Maaf, coba lagi", Toast.LENGTH_SHORT).show()
                                } else {
                                    teamList.adapter =
                                            TeamListAdapter(this@TeamListActivity, filteredData, { filteredData ->
                                                toDetailTeam(filteredData)
                                            })
                                    Log.i("DATANEXT", filteredData.toString())
                                }

                            } catch (e: NullPointerException) {
                                Log.i("ERROR", "NullPointerException")
                            }
                        }

                        override fun onFailed(response: String) {
                            Log.i("ERROR", response)
                        }

                        override fun onFailure(throwable: Throwable?) {
                            Toast.makeText(this@TeamListActivity, "No connection?", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun showLoading() {
        teambar.visible()
    }

    override fun hideLoading() {
        teambar.invisible()
    }
}
