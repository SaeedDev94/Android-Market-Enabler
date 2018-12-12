package room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(
    tableName = "providers",
    foreignKeys = {
        @ForeignKey(
            entity = Country.class,
            parentColumns = "id",
            childColumns = "country_id",
            onUpdate = CASCADE,
            onDelete = CASCADE
        )
    }
)
public class Provider
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "country_id")
    private long countryId;

    @ColumnInfo(name = "code")
    private String code;

    @ColumnInfo(name = "name")
    private String name;

    public Provider(long countryId, String code, String name)
    {
        this.countryId = countryId;
        this.code = code;
        this.name = name;
    }

    public long getId()
    {
        return this.id;
    }

    public long getCountryId()
    {
        return this.countryId;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getName()
    {
        return this.name;
    }

    public void setId(long id)
    {
        this.id = id;
    }
}
