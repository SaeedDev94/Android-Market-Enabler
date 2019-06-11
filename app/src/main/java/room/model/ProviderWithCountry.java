package room.model;

import androidx.room.ColumnInfo;

public class ProviderWithCountry
{
    @ColumnInfo(name = "provider_id")
    private long providerId;

    @ColumnInfo(name = "provider_code")
    private String providerCode;

    @ColumnInfo(name = "provider_name")
    private String providerName;

    @ColumnInfo(name = "country_id")
    private long countryId;

    @ColumnInfo(name = "country_iso")
    private String countryIso;

    @ColumnInfo(name = "country_name")
    private String countryName;

    public ProviderWithCountry(long providerId, String providerCode, String providerName, long countryId, String countryIso, String countryName)
    {
        this.providerId = providerId;
        this.providerCode = providerCode;
        this.providerName = providerName;
        this.countryId = countryId;
        this.countryIso = countryIso;
        this.countryName = countryName;
    }

    public long getProviderId()
    {
        return this.providerId;
    }

    public String getProviderCode()
    {
        return this.providerCode;
    }

    public String getProviderName()
    {
        return this.providerName;
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
}
