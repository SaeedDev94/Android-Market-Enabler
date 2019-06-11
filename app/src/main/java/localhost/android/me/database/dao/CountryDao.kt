package localhost.android.me.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import localhost.android.me.database.entity.Country

@Dao
interface CountryDao
{
    @Insert
    fun insert(country: Country)

    @Query("SELECT * FROM countries")
    fun getAll(): List<Country>

    @Query("SELECT * FROM countries WHERE countries.name = :name LIMIT 1")
    fun getByName(name: String): List<Country>
}
