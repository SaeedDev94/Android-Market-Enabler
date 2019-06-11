package localhost.android.me.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import localhost.android.me.database.entity.Provider
import localhost.android.me.database.model.ProviderWithCountry

@Dao
interface ProviderDao
{
    @Insert
    fun insert(provider: Provider)

    @Update
    fun update(provider: Provider)

    @Delete
    fun delete(provider: Provider)

    @Query(
        "SELECT providers.id as provider_id, countries.id as country_id, " +
        "countries.iso as country_iso, countries.name as country_name, " +
        "providers.code as provider_code, providers.name as provider_name FROM providers " +
        "JOIN countries ON providers.country_id = countries.id " +
        "ORDER BY providers.id DESC"
    )
    fun getAll(): List<ProviderWithCountry>
}
