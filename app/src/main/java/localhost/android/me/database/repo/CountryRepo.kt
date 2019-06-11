package localhost.android.me.database.repo

import android.content.Context
import localhost.android.me.database.dao.CountryDao
import localhost.android.me.database.MarketEnablerDatabase
import localhost.android.me.database.entity.Country

class CountryRepo(private var context: Context)
{
    private var countryDao: CountryDao

    init {
        val db = MarketEnablerDatabase.getDatabase(this.context)
        this.countryDao = db.countryDao()
    }

    fun getCountries(): List<Country>
    {
        val getAll = GetAll(this.countryDao)
        val thread = Thread(getAll)
        thread.start()
        thread.join()
        return getAll.list()
    }

    fun getCountryByName(name: String): List<Country>
    {
        val getByName = GetByName(this.countryDao, name)
        val thread = Thread(getByName)
        thread.start()
        thread.join()
        return getByName.country()
    }

    class GetAll(private var countryDao: CountryDao) : Runnable
    {
        @Volatile
        private lateinit var countries: List<Country>

        override fun run()
        {
            this.countries = this.countryDao.getAll()
        }

        fun list(): List<Country>
        {
            return this.countries
        }
    }

    class GetByName(
        private var countryDao: CountryDao,
        private var name: String
    ) : Runnable
    {
        @Volatile
        private lateinit var country: List<Country>

        override fun run()
        {
            this.country = this.countryDao.getByName(this.name)
        }

        fun country(): List<Country>
        {
            return this.country
        }
    }
}
