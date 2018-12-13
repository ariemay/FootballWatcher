package ariemaywibowo.footballwatcher.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ariemaywibowo.footballwatcher.matchesFragments.LastMatchesFragment
import ariemaywibowo.footballwatcher.matchesFragments.NextMatchesFragment

class MatchTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> LastMatchesFragment()
            else -> {
                NextMatchesFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Last Matches"
            else -> {
                "Next Matches"
            }
        }
    }

}