package ariemaywibowo.footballwatcher.server

import ariemaywibowo.footballwatcher.BuildConfig

class FootballAPI {

    companion object {
        // base end point
        const val END_POINT = BuildConfig.BASE_URL + "api/v1/json/${BuildConfig.TSDB_API_KEY}" + "/"

        const val eventsPastleague = "eventspastleague.php"

        const val eventsNextleague = "eventsnextleague.php"

        const val lookupEvent = "lookupevent.php"

        const val lookupteam = "lookupteam.php"

        const val searchevents = "searchevents.php"

        const val lookupallteam = "lookup_all_teams.php"

        const val lookup_all_players = "lookup_all_players.php"

        const val lookupplayer = "lookupplayer.php"

        const val searchteams = "searchteams.php"
    }
}