package localhost.android.me.database

import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import org.json.JSONException
import org.json.JSONObject
import localhost.android.me.database.dao.CountryDao
import localhost.android.me.database.dao.ProviderDao
import localhost.android.me.database.entity.Country
import localhost.android.me.database.entity.Provider

@Database(
    entities = [
        Country::class,
        Provider::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MarketEnablerDatabase : RoomDatabase()
{
    abstract fun countryDao(): CountryDao
    abstract fun providerDao(): ProviderDao

    companion object {
        @Volatile
        private var db: MarketEnablerDatabase? = null
        private var name: String = "market_enabler_database"

        fun getDatabase(context: Context): MarketEnablerDatabase
        {
            if (db != null) return db!!
            synchronized(this) {
                db = Room.databaseBuilder(
                    context.applicationContext,
                    MarketEnablerDatabase::class.java,
                    name
                ).addCallback(
                    object: RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            val seeder = Thread(DatabaseSeeder(context))
                            seeder.start()
                        }
                    }
                ).build()
                return db!!
            }
        }
    }

    class DatabaseSeeder(private var context: Context) :  Runnable
    {
        override fun run()
        {
            val jsonStr = this.context.resources.assets.open("json/countries.json").bufferedReader().use { it.readText() }
            try
            {
                val json = JSONObject(jsonStr)
                val countries = json.getJSONArray("countries")
                for (i in 0 until countries.length())
                {
                    val countryObj = countries[i] as JSONObject
                    val iso = countryObj.getString("Code")
                    val name = countryObj.getString("Name")
                    val country = Country(iso, name)
                    MarketEnablerDatabase.getDatabase(this.context).countryDao().insert(country)
                }
            }
            catch (error: JSONException)
            {
                error.printStackTrace()
            }
        }
    }
}
