package ariemaywibowo.footballwatcher.matchesFragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ariemaywibowo.footballwatcher.adapter.NextMatchesAdapter
import ariemaywibowo.footballwatcher.models.EventsItem
import ariemaywibowo.footballwatcher.R
import kotlinx.android.synthetic.main.fragment_next_matches.*

class NextMatchesFragment : Fragment() {

    lateinit var dataMatches : List<EventsItem>
    private var newEvents : MutableList<EventsItem> = mutableListOf()
    private lateinit var nextMatchesAdapter : NextMatchesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_next_matches, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val nextMatches = next_matches
        nextMatches.layoutManager = LinearLayoutManager(context)
    }

    fun letsFilter(filterer : String?) {
        Log.i("LETFILTER1", filterer)
        val dataFilterer = dataMatches.filter {
            (it.strHomeTeam?.contains(filterer.toString(), true)?: false)
                    || (it.strAwayTeam?.contains(filterer.toString(), true)?: false) }
        newEvents.clear()
        newEvents.addAll(dataFilterer)
        Log.i("NEWEVENTS", newEvents.toString())
        nextMatchesAdapter.notifyDataSetChanged()
    }
}