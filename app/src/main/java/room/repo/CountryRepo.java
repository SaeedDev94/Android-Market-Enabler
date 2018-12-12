package room.repo;

import android.content.Context;
import java.util.List;
import room.dao.CountryDao;
import room.database.MarketEnablerDatabase;
import room.entity.Country;

public class CountryRepo
{
    private CountryDao countryDao;

    public CountryRepo(Context context)
    {
        MarketEnablerDatabase db = MarketEnablerDatabase.getDatabase(context);
        this.countryDao = db.countryDao();
    }

    public List<Country> getCountries() throws InterruptedException
    {
        GetAll getAll = new GetAll(this.countryDao);
        Thread thread = new Thread(getAll);
        thread.start();
        thread.join();
        return getAll.list();
    }

    public List<Country> getCountryByName(String name) throws InterruptedException
    {
        GetByName getByName = new GetByName(this.countryDao, name);
        Thread thread = new Thread(getByName);
        thread.start();
        thread.join();
        return getByName.country();
    }

    public class GetAll implements Runnable
    {
        private CountryDao countryDao;
        private volatile List<Country> countries;

        GetAll(CountryDao countryDao)
        {
            this.countryDao = countryDao;
        }

        @Override
        public void run()
        {
            this.countries = this.countryDao.getAll();
        }

        private List<Country> list()
        {
            return this.countries;
        }
    }

    public class GetByName implements Runnable
    {
        private CountryDao countryDao;
        private String name;
        private volatile List<Country> country;

        GetByName(CountryDao countryDao, String name)
        {
            this.countryDao = countryDao;
            this.name = name;
        }

        @Override
        public void run()
        {
            this.country = this.countryDao.getByName(this.name);
        }

        private List<Country> country()
        {
            return this.country;
        }
    }
}
