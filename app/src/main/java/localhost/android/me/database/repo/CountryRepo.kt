package localhost.android.me.database.repo

import android.content.Context
import android.os.AsyncTask
import localhost.android.me.database.Callback
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

    fun getCountries(resultCallback: Callback.OnCountryResultListener)
    {
        GetAllTask(this.countryDao, resultCallback).execute()
    }

    fun getCountryByName(name: String, resultCallback: Callback.OnCountryResultListener)
    {
        GetByNameTask(this.countryDao, name, resultCallback).execute()
    }

    class GetAllTask(
            private var countryDao: CountryDao,
            private var resultCallback: Callback.OnCountryResultListener
    ) : AsyncTask<Void?, Void?, List<Country>>()
    {
        override fun doInBackground(vararg p0: Void?): List<Country>
        {
            return this.countryDao.getAll()
        }

        override fun onPostExecute(countries: List<Country>)
        {
            this.resultCallback.onResult(countries)
        }
    }

    class GetByNameTask(
        private var countryDao: CountryDao,
        private var name: String,
        private var resultCallback: Callback.OnCountryResultListener
    ) : AsyncTask<Void?, Void?, List<Country>>()
    {
        override fun doInBackground(vararg p0: Void?): List<Country>
        {
            return this.countryDao.getByName(name)
        }

        override fun onPostExecute(countries: List<Country>)
        {
            this.resultCallback.onResult(countries)
        }
    }
}
