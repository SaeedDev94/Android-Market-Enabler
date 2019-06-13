package localhost.android.me.database.entity

import androidx.room.*
import androidx.room.ForeignKey.CASCADE

@Entity(
    tableName = "providers",
    foreignKeys = [
        ForeignKey(
            entity = Country::class,
            parentColumns = ["id"],
            childColumns = ["country_id"],
            onUpdate = CASCADE,
            onDelete = CASCADE
        )
    ],
    indices = [
        Index(
            name = "providers_country_id_foreign",
            value = ["country_id"]
        )
    ]
)
class Provider(
    @ColumnInfo(name = "country_id") private var countryId: Long,
    @ColumnInfo(name = "code") private var code: String,
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

    fun getCountryId(): Long
    {
        return this.countryId
    }

    fun getCode(): String
    {
        return this.code
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
