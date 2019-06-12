package localhost.android.me.database.repo

import android.content.Context
import android.os.AsyncTask
import localhost.android.me.database.Callback
import localhost.android.me.database.dao.ProviderDao
import localhost.android.me.database.MarketEnablerDatabase
import localhost.android.me.database.entity.Provider
import localhost.android.me.database.model.ProviderWithCountry

class ProviderRepo(private var context: Context)
{
    private var providerDao: ProviderDao

    init {
        val db = MarketEnablerDatabase.getDatabase(this.context)
        this.providerDao = db.providerDao()
    }

    fun getProviders(resultCallback: Callback.OnProviderResultListener)
    {
        GetAllTask(this.providerDao, resultCallback).execute()
    }

    fun insertProvider(provider: Provider, doneCallback: Callback.OnDoneListener)
    {
        InsertTask(this.providerDao, provider, doneCallback).execute()
    }

    fun updateProvider(provider: Provider, doneCallback: Callback.OnDoneListener)
    {
        UpdateTask(this.providerDao, provider, doneCallback).execute()
    }

    fun deleteProvider(provider: Provider)
    {
        DeleteTask(this.providerDao, provider).execute()
    }

    class GetAllTask(
        private var providerDao: ProviderDao,
        private var resultCallback: Callback.OnProviderResultListener
    ) : AsyncTask<Void?, Void, List<ProviderWithCountry>>()
    {
        override fun doInBackground(vararg p0: Void?): List<ProviderWithCountry>
        {
            return this.providerDao.getAll()
        }

        override fun onPostExecute(providers: List<ProviderWithCountry>)
        {
            this.resultCallback.onResult(providers)
        }
    }

    class InsertTask(
        private var providerDao: ProviderDao,
        private var provider: Provider,
        private var doneCallback: Callback.OnDoneListener
    ) : AsyncTask<Void?, Void?, Void?>()
    {
        override fun doInBackground(vararg p0: Void?): Void?
        {
            this.providerDao.insert(this.provider)
            return null
        }

        override fun onPostExecute(result: Void?)
        {
            this.doneCallback.onSuccess()
        }
    }

    class UpdateTask(
        private var providerDao: ProviderDao,
        private var provider: Provider,
        private var doneCallback: Callback.OnDoneListener
    ) : AsyncTask<Void?, Void?, Void?>()
    {
        override fun doInBackground(vararg p0: Void?): Void?
        {
            this.providerDao.update(this.provider)
            return null
        }

        override fun onPostExecute(result: Void?)
        {
            this.doneCallback.onSuccess()
        }
    }

    class DeleteTask(
        private var providerDao: ProviderDao,
        private var provider: Provider
    ) : AsyncTask<Void?, Void?, Void?>()
    {
        override fun doInBackground(vararg p0: Void?): Void?
        {
            this.providerDao.delete(this.provider)
            return null
        }
    }
}
