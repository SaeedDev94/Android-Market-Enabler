package localhost.android.me.database.repo;

import android.content.Context;
import java.util.List;
import localhost.android.me.database.dao.ProviderDao;
import localhost.android.me.database.MarketEnablerDatabase;
import localhost.android.me.database.entity.Provider;
import localhost.android.me.database.model.ProviderWithCountry;

public class ProviderRepo
{
    private ProviderDao providerDao;

    public ProviderRepo(Context context)
    {
        MarketEnablerDatabase db = MarketEnablerDatabase.getDatabase(context);
        this.providerDao = db.providerDao();
    }

    public List<ProviderWithCountry> getProviders() throws InterruptedException
    {
        GetAll getAll = new GetAll(this.providerDao);
        Thread thread = new Thread(getAll);
        thread.start();
        thread.join();
        return getAll.list();
    }

    public void insertProvider(Provider provider) throws InterruptedException
    {
        Insert insert = new Insert(this.providerDao, provider);
        Thread thread = new Thread(insert);
        thread.start();
        thread.join();
    }

    public void updateProvider(Provider provider) throws InterruptedException
    {
        Update update = new Update(this.providerDao, provider);
        Thread thread = new Thread(update);
        thread.start();
        thread.join();
    }

    public void deleteProvider(Provider provider) throws InterruptedException
    {
        Delete delete = new Delete(this.providerDao, provider);
        Thread thread = new Thread(delete);
        thread.start();
        thread.join();
    }

    public class GetAll implements Runnable
    {
        private ProviderDao providerDao;
        private volatile List<ProviderWithCountry> providers;

        GetAll(ProviderDao providerDao)
        {
            this.providerDao = providerDao;
        }

        @Override
        public void run()
        {
            this.providers = this.providerDao.getAll();
        }

        private List<ProviderWithCountry> list()
        {
            return this.providers;
        }
    }

    public class Insert implements Runnable
    {
        private ProviderDao providerDao;
        private Provider provider;

        Insert(ProviderDao providerDao, Provider provider)
        {
            this.providerDao = providerDao;
            this.provider = provider;
        }

        @Override
        public void run()
        {
            this.providerDao.insert(this.provider);
        }
    }

    public class Update implements Runnable
    {
        private ProviderDao providerDao;
        private Provider provider;

        Update(ProviderDao providerDao, Provider provider)
        {
            this.providerDao = providerDao;
            this.provider = provider;
        }

        @Override
        public void run()
        {
            this.providerDao.update(this.provider);
        }
    }

    public class Delete implements Runnable
    {
        private ProviderDao providerDao;
        private Provider provider;

        Delete(ProviderDao providerDao, Provider provider)
        {
            this.providerDao = providerDao;
            this.provider = provider;
        }

        @Override
        public void run()
        {
            this.providerDao.delete(this.provider);
        }
    }
}
