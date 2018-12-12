package room.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import room.dao.CountryDao;
import room.dao.ProviderDao;
import room.entity.Country;
import room.entity.Provider;
import helper.AssetHelper;

@Database(
    entities = {
        Country.class,
        Provider.class
    },
    version = 1,
    exportSchema = false
)
public abstract class MarketEnablerDatabase extends RoomDatabase
{
    private static volatile MarketEnablerDatabase db;

    public abstract CountryDao countryDao();
    public abstract ProviderDao providerDao();

    public static synchronized MarketEnablerDatabase getDatabase(final Context context)
    {
        if (db != null)
        {
            return db;
        }
        RoomDatabase.Callback databaseSeeder = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db)
            {
                super.onCreate(db);
                Thread seeder = new Thread(new DatabaseSeeder(context));
                seeder.start();
            }
        };
        db = Room.databaseBuilder(
                context.getApplicationContext(),
                MarketEnablerDatabase.class,
                "market_enabler_database"
        ).addCallback(databaseSeeder).build();
        return db;
    }

    static class DatabaseSeeder implements Runnable
    {
        Context context;

        DatabaseSeeder(Context context)
        {
            this.context = context;
        }

        @Override
        public void run()
        {
            AssetHelper json = new AssetHelper(this.context, "json/countries.json");
            if (!json.setString())
            {
                return;
            }
            try
            {
                JSONObject jsonObject = new JSONObject(json.getString());
                JSONArray countries = jsonObject.getJSONArray("countries");
                int countriesLen = countries.length();
                for (int i = 0 ; i < countriesLen ; i++)
                {
                    JSONObject countryObject = countries.getJSONObject(i);
                    String iso = countryObject.getString("Code");
                    String name = countryObject.getString("Name");
                    Country country = new Country(iso, name);
                    MarketEnablerDatabase.getDatabase(this.context).countryDao().insert(country);
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}
