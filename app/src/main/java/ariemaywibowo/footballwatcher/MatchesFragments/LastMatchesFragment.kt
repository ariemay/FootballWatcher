package ariemaywibowo.footballwatcher.MatchesFragments

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import ariemaywibowo.footballwatcher.Adapter.LastMatchesAdapter
import ariemaywibowo.footballwatcher.Adapter.NextMatchesAdapter
import ariemaywibowo.footballwatcher.Models.EventsItem
import ariemaywibowo.footballwatcher.Models.LeaguesItem
import ariemaywibowo.footballwatcher.Presenter.MatchPresenter
import ariemaywibowo.footballwatcher.R
import ariemaywibowo.footballwatcher.Utility.OutputServerStats
import ariemaywibowo.footballwatcher.Utility.invisible
import ariemaywibowo.footballwatcher.Utility.visible
import ariemaywibowo.footballwatcher.View.Details.DetailActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import kotlinx.android.synthetic.main.fragment_last_matches.*
import org.json.JSONArray
import org.json.JSONObject


class LastMatchesFragment : Fragment() {

    lateinit var dataMatches : List<EventsItem>
    private var newEvents : MutableList<EventsItem> = mutableListOf()
    private lateinit var lastMatchesAdapter : LastMatchesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_last_matches, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val lastMatches = last_matches
        lastMatches.layoutManager = LinearLayoutManager(context)

    }

    fun letsFilter(filterer : String?) {
        Log.i("LETFILTER1", filterer)
        val dataFilterer = dataMatches.filter {
            (it.strHomeTeam?.contains(filterer.toString(), true)?: false)}
        newEvents.clear()
        newEvents.addAll(dataFilterer)
        Log.i("NEWEVENTS", newEvents.toString())
        lastMatchesAdapter.notifyDataSetChanged()
    }

}
