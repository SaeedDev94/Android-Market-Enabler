package localhost.android.me.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;
import localhost.android.me.database.entity.Country;

@Dao
public interface CountryDao
{
    @Insert
    void insert(Country country);

    @Query("SELECT * FROM countries")
    List<Country> getAll();

    @Query("SELECT * FROM countries WHERE countries.name = :name LIMIT 1")
    List<Country> getByName(String name);
}