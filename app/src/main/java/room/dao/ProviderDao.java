package room.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import java.util.List;
import room.entity.Provider;
import room.model.ProviderWithCountry;

@Dao
public interface ProviderDao
{
    @Insert
    void insert(Provider provider);

    @Update
    void update(Provider provider);

    @Delete
    void delete(Provider provider);

    @Query(
        "SELECT providers.id as provider_id, countries.id as country_id, " +
        "countries.iso as country_iso, countries.name as country_name, " +
        "providers.code as provider_code, providers.name as provider_name FROM providers " +
        "JOIN countries ON providers.country_id = countries.id " +
        "ORDER BY providers.id DESC"
    )
    List<ProviderWithCountry> getAll();
}
