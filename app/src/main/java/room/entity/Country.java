package room.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "countries")
public class Country
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @ColumnInfo(name = "iso")
    private String iso;

    @ColumnInfo(name = "name")
    private String name;

    public Country(String iso, String name)
    {
        this.iso = iso;
        this.name = name;
    }

    public long getId()
    {
        return this.id;
    }

    public String getIso()
    {
        return this.iso;
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
