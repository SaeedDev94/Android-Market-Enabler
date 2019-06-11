package localhost.android.me.model;

public class ProviderModel
{
    private long id;
    private String code;
    private String name;
    private long countryId;
    private String countryIso;
    private String countryName;
    private boolean custom;

    public ProviderModel(String code, String countryIso, String name, String custom)
    {
        this.code = code;
        this.countryIso = countryIso;
        this.name = name;
        this.custom =  Boolean.parseBoolean(custom);
    }

    public long getId()
    {
        return this.id;
    }

    public String getCode()
    {
        return this.code;
    }

    public String getName()
    {
        return this.name;
    }

    public String getFullTitle()
    {
        return "[" + this.countryIso + "] " + this.name;
    }

    public long getCountryId()
    {
        return this.countryId;
    }

    public String getCountryIso()
    {
        return this.countryIso;
    }

    public String getCountryName()
    {
        return this.countryName;
    }

    public boolean isCustom()
    {
        return this.custom;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public void setCountryId(long countryId)
    {
        this.countryId = countryId;
    }

    public void setCountryName(String countryName)
    {
        this.countryName = countryName;
    }
}
