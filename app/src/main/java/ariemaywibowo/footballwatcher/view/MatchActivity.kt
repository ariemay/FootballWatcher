package ariemaywibowo.footballwatcher.view

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.widget.SwipeRefreshLayout
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.support.v7.widget.SearchView
import android.widget.Toast
import ariemaywibowo.footballwatcher.adapter.LastMatchesAdapter
import ariemaywibowo.footballwatcher.adapter.MatchTabAdapter
import ariemaywibowo.footballwatcher.adapter.NextMatchesAdapter
import ariemaywibowo.footballwatcher.matchesFragments.MainView
import ariemaywibowo.footballwatcher.models.EventsItem
import ariemaywibowo.footballwatcher.models.LeaguesItem
import ariemaywibowo.footballwatcher.presenter.MatchPresenter
import ariemaywibowo.footballwatcher.presenter.SearchPresenter
import ariemaywibowo.footballwatcher.R
import ariemaywibowo.footballwatcher.utility.OutputServerStats
import ariemaywibowo.footballwatcher.utility.invisible
import ariemaywibowo.footballwatcher.utility.visible
import ariemaywibowo.footballwatcher.view.details.DetailActivity
import ariemaywibowo.footballwatcher.view.teams.TeamListActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.activity_match.*
import kotlinx.android.synthetic.main.fragment_last_matches.*
import kotlinx.android.synthetic.main.fragment_next_matches.*
import org.json.JSONArray
import org.json.JSONObject

class MatchActivity : AppCompatActivity(), MainView {

    private val presenter = MatchPresenter(this)
    private val searchPresenter = SearchPresenter()
    private lateinit var spinnerID : String
    private lateinit var swipeRefresh : SwipeRefreshLayout
    private var listLeague = ArrayList<LeaguesItem>()
    private var menu: Int = 1
    private var passItem : ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match)

        val fragmentAdapter = MatchTabAdapter(supportFragmentManager)
        viewpager_main.adapter = fragmentAdapter

        tabs_main.setupWithViewPager(viewpager_main)

        allLeagues()
        callSpinner(menu)
        startBottomNav()

        swipeRefresh = goSwipeRefresh
        swipeRefresh.setOnRefreshListener {
            if (swipeRefresh.isRefreshing) {
                swipeRefresh.isRefreshing = false
                showLoading()
                callSpinner(menu)
                containerToShow(spinnerID, menu)
                hideLoading()
            }

        }

    }

    override fun onResume() {
        super.onResume()
        if(menu==2){
            refreshFavorite()
        }
    }

    private fun toNextActivity(eventsItem: EventsItem) {
        val idEvent = eventsItem.idEvent.toString()
        val dataMatch  = eventsItem
        passItem?.clear()
        passItem = arrayListOf(eventsItem.idHomeTeam.toString(), eventsItem.idAwayTeam.toString())
        val showDetailActivityIntent = Intent(this, DetailActivity::class.java)
        showDetailActivityIntent.putExtra("PASSEDITEM", passItem)
        showDetailActivityIntent.putExtra("ID_EVENTS", idEvent)
        showDetailActivityIntent.putExtra("TOFAV", dataMatch)
        startActivity(showDetailActivityIntent)
    }

    private fun allLeagues() {

        val queue = Volley.newRequestQueue(this)
        val url: String = "https://www.thesportsdb.com/api/v1/json/1/all_leagues.php"

        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                val leagueList = response.toString()
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
                leagueSpinner.adapter = ArrayAdapter<String>(this@MatchActivity,
                    android.R.layout.simple_spinner_dropdown_item, strLeague)
            },
            Response.ErrorListener { Log.i("ERROR NIH", "league error") })
        queue.add(stringReq)
    }

    private fun callSpinner(navMenu: Int) {
        leagueSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

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

    private fun startBottomNav() {
        navigationButton.setOnNavigationItemSelectedListener(bottomNavigationListener)
    }

    private fun containerToShow(spinnerID: String, navMenu: Int) {
        var data: MutableList<EventsItem>
        if (navMenu == 1) {
            showLoading()
            presenter.getLastMatches(spinnerID, object : OutputServerStats {

                override fun onSuccess(response: String) {
                    Log.i("RESPONSE", response)
                    try {
                        data = presenter.parsingData(response)
                        if (data.size < 1) {
                            no_data_layout.visibility = View.VISIBLE
                            Toast.makeText(this@MatchActivity, "Maaf, coba lagi", Toast.LENGTH_SHORT).show()
                        } else {

                            no_data_layout.visibility = View.GONE
                            Log.i("DATALAST1", data.toString())
                            last_matches.adapter = LastMatchesAdapter(this@MatchActivity, data, {data -> toNextActivity(data) })
                            Log.i("DATALAST2", data.toString())
                        }

                    } catch (e: NullPointerException) {
                        Log.i("ERROR", "NullPointerException")
                    }
                }

                override fun onFailed(response: String) {
                    Log.i("ERROR", response)
                }

                override fun onFailure(throwable: Throwable?) {
                    Toast.makeText(this@MatchActivity, "No connection?", Toast.LENGTH_SHORT).show()
                }
            })
            presenter.getNextMatch(spinnerID, object : OutputServerStats {

                override fun onSuccess(response: String) {
                    Log.i("RESPONSE", response)
                    try {
                        data = presenter.parsingData(response)
                        if (data.size < 1) {
                            no_data_layout.visibility = View.VISIBLE
                            viewpager_main.visibility = View.GONE
                            Toast.makeText(this@MatchActivity, "Maaf, coba lagi", Toast.LENGTH_SHORT).show()
                        } else {
                            no_data_layout.visibility = View.GONE
                            viewpager_main.visibility = View.VISIBLE
                            next_matches.adapter = NextMatchesAdapter(this@MatchActivity, data, { data -> toNextActivity(data) })
                            Log.i("DATANEXT", data.toString())
                        }

                    } catch (e: NullPointerException) {
                        Log.i("ERROR", "NullPointerException")
                    }
                }

                override fun onFailed(response: String) {
                    Log.i("ERROR", response)
                }

                override fun onFailure(throwable: Throwable?) {
                    Toast.makeText(this@MatchActivity, "No connection?", Toast.LENGTH_SHORT).show()
                }
            })
            hideLoading()
        } else if (menu == 2) {
            val data : MutableList<EventsItem>
            Log.i("DATAFAV", "start?")
            data = presenter.getFav(this@MatchActivity)
            if (data.size < 1) {
                no_data_layout.visibility = View.VISIBLE
                viewpager_main.visibility = View.GONE
                Toast.makeText(this@MatchActivity, "No Data", Toast.LENGTH_SHORT).show()
            } else {
                no_data_layout.visibility = View.GONE
                viewpager_main.visibility = View.VISIBLE
                leagueSpinner.visibility = View.GONE
                tabs_main.visibility = View.GONE
                last_matches.adapter = LastMatchesAdapter(this@MatchActivity, data, { data -> toNextActivity(data) })
                next_matches.adapter = NextMatchesAdapter(this@MatchActivity, data, { data -> toNextActivity(data) })
            }
        }
    }

    private fun refreshFavorite(){
        val data : MutableList<EventsItem>
        Log.i("DATAFAV", "start?")
        data = presenter.getFav(this@MatchActivity)
        if (data.size < 1) {
            no_data_layout.visibility = View.VISIBLE
            Toast.makeText(this@MatchActivity, "No Data", Toast.LENGTH_SHORT).show()
        } else {

            no_data_layout.visibility = View.GONE
            leagueSpinner.visibility = View.INVISIBLE
            tabs_main.visibility = View.INVISIBLE
            last_matches.adapter = LastMatchesAdapter(this@MatchActivity, data, { data -> toNextActivity(data) })
            next_matches.adapter = NextMatchesAdapter(this@MatchActivity, data, { data -> toNextActivity(data) })
        }
    }

    private fun goToTeamList() {
        val getTeamList = Intent(this, TeamListActivity::class.java)
        startActivity(getTeamList)
    }

    private val bottomNavigationListener by lazy {
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            Log.i("BOTNAV", item.toString())
            when (item.itemId) {
                R.id.Matches -> {
                    leagueSpinner.visibility = View.VISIBLE
                    tabs_main.visibility = View.VISIBLE
                    menu = 1
                    title = getString(R.string.Matches)
                    callSpinner(menu)
                    containerToShow(spinnerID, menu)
                    Log.d("ACT", "MATCHES")
                    true
                }
                R.id.favorites -> {
                    menu = 2
                    title = getString(R.string.favorites)
                    containerToShow("0", menu)
                    Log.d("ACT", "FAV")
                    true
                }
                R.id.teams -> {
                    menu = 3
                    title = getString(R.string.Team)
                    goToTeamList()
                    true
                }
                else -> {
                    true
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchManager = this@MatchActivity.getSystemService(Context.SEARCH_SERVICE) as SearchManager
        var searchView: SearchView? = null
        if (searchItem != null) {
            searchView = searchItem.actionView as SearchView
        }
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(this@MatchActivity.componentName))
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                Log.i("ONSUBMIT", p0)
                if (p0 != null) {
                    var filteredData: MutableList<EventsItem>
                    presenter.searchEvents(p0, object : OutputServerStats {

                        override fun onSuccess(response: String) {
                            Log.i("RESPONSE QUERY", response)
                            try {
                                filteredData = searchPresenter.parsingSearch(response)
                                Log.i("FILTERDATA", filteredData.toString())
                                if (filteredData.size < 1) {
                                    Toast.makeText(this@MatchActivity, "Maaf, coba lagi", Toast.LENGTH_SHORT).show()
                                } else {
                                    next_matches.adapter =
                                            NextMatchesAdapter(this@MatchActivity, filteredData, { filteredData ->
                                                toNextActivity(filteredData)
                                            })
                                    last_matches.adapter =
                                            LastMatchesAdapter(this@MatchActivity, filteredData, { filteredData ->
                                                toNextActivity(filteredData)
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
                            Toast.makeText(this@MatchActivity, "No connection?", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                Log.i("TEXTCHANGE", p0)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun showLoading() {
        proBar.visible()
    }

    override fun hideLoading() {
        proBar.invisible()
    }
}
