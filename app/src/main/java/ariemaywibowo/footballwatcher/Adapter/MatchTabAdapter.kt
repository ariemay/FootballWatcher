package ariemaywibowo.footballwatcher.Adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import ariemaywibowo.footballwatcher.MatchesFragments.LastMatchesFragment
import ariemaywibowo.footballwatcher.MatchesFragments.NextMatchesFragment

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