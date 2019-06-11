package localhost.android.me.database.repo

import android.content.Context
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

    fun getProviders(): List<ProviderWithCountry>
    {
        val getAll = GetAll(this.providerDao)
        val thread = Thread(getAll)
        thread.start()
        thread.join()
        return getAll.list()
    }

    fun insertProvider(provider: Provider)
    {
        val insert = Insert(this.providerDao, provider)
        val thread = Thread(insert)
        thread.start()
        thread.join()
    }

    fun updateProvider(provider: Provider)
    {
        val update = Update(this.providerDao, provider)
        val thread = Thread(update)
        thread.start()
        thread.join()
    }

    fun deleteProvider(provider: Provider)
    {
        val delete = Delete(this.providerDao, provider)
        val thread = Thread(delete)
        thread.start()
        thread.join()
    }

    class GetAll(private var providerDao: ProviderDao) : Runnable
    {
        @Volatile
        private lateinit var providers: List<ProviderWithCountry>

        override fun run()
        {
            this.providers = this.providerDao.getAll()
        }

        fun list(): List<ProviderWithCountry>
        {
            return this.providers
        }
    }

    class Insert(
        private var providerDao: ProviderDao,
        private var provider: Provider
    ) : Runnable
    {
        override fun run()
        {
            this.providerDao.insert(this.provider)
        }
    }

    class Update(
        private var providerDao: ProviderDao,
        private var provider: Provider
    ) : Runnable
    {
        override fun run()
        {
            this.providerDao.update(this.provider)
        }
    }

    class Delete(
        private var providerDao: ProviderDao,
        private var provider: Provider
    ) : Runnable
    {
        override fun run()
        {
            this.providerDao.delete(this.provider)
        }
    }
}
