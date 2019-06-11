package localhost.android.me.database.model

import androidx.room.ColumnInfo

class ProviderWithCountry(
    @ColumnInfo(name = "provider_id") private var providerId: Long,
    @ColumnInfo(name = "provider_code") private var providerCode: String,
    @ColumnInfo(name = "provider_name") private var providerName: String,
    @ColumnInfo(name = "country_id") private var countryId: Long,
    @ColumnInfo(name = "country_iso") private var countryIso: String,
    @ColumnInfo(name = "country_name") private var countryName: String
)
{
    fun getProviderId(): Long
    {
        return this.providerId
    }

    fun getProviderCode(): String
    {
        return this.providerCode
    }

    fun getProviderName(): String
    {
        return this.providerName
    }

    fun getCountryId(): Long
    {
        return this.countryId
    }

    fun getCountryIso(): String
    {
        return this.countryIso
    }

    fun getCountryName(): String
    {
        return this.countryName
    }
}
