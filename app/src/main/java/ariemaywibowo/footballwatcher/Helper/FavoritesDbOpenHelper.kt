package ariemaywibowo.footballwatcher.Helper

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import ariemaywibowo.footballwatcher.Models.EventsItem
import ariemaywibowo.footballwatcher.Models.TeamsItem

class FavoritesDbOpenHelper(context : Context) : ManagedSQLiteOpenHelper(context, "FavMatch.db", null, 1) {

    companion object {
        private var instance : FavoritesDbOpenHelper? = null
        @Synchronized
        fun getInstance(context: Context) : FavoritesDbOpenHelper {
            if (instance == null) {
                instance = FavoritesDbOpenHelper(context.applicationContext)
            }
            return instance as FavoritesDbOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(EventsItem.TABLE_FAVORITES, true,
                EventsItem.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                EventsItem.DATE_EVENT to TEXT,
                EventsItem.TIME_EVENT to TEXT,
                EventsItem.ID_AWAY_TEAM to TEXT,
                EventsItem.ID_EVENT to TEXT,
                EventsItem.ID_HOME_TEAM to TEXT,
                EventsItem.ID_LEAGUE to TEXT,
                EventsItem.INT_AWAY_SCORE to TEXT,
                EventsItem.INT_HOME_SCORE to TEXT,
                EventsItem.STR_AWAY_TEAM to TEXT,
                EventsItem.STR_HOME_TEAM to TEXT)

        db.createTable(TeamsItem.TABLE_TEAM_FAV, true,
                TeamsItem.IDLONG to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
                TeamsItem.ID_TEAM to TEXT,
                TeamsItem.STR_TEAM to TEXT,
                TeamsItem.STR_TEAM_BADGE to TEXT,
                TeamsItem.STR_STADIUM to TEXT,
                TeamsItem.STR_MANAGER to TEXT,
                TeamsItem.STR_TEAMART to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            db.dropIndex(EventsItem.TABLE_FAVORITES, true)
        }
    }
}

val Context.database: FavoritesDbOpenHelper
    get() = FavoritesDbOpenHelper.getInstance(applicationContext)