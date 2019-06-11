package localhost.android.me.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries")
class Country(
    @ColumnInfo(name = "iso") private var iso: String,
    @ColumnInfo(name = "name") private var name: String
)
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private var id: Long = 0L

    fun getId(): Long
    {
        return this.id
    }

    fun getIso(): String
    {
        return this.iso
    }

    fun getName(): String
    {
        return this.name
    }

    fun setId(id: Long)
    {
        this.id = id
    }
}
