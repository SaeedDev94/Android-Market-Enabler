package localhost.android.me.model

class ProviderModel(
    private var code: String,
    private var countryIso: String,
    private var name: String,
    custom: String
)
{
    private var id: Long = 0
    private var countryId: Long = 0
    private var countryName: String = ""
    private var custom: Boolean = custom.toBoolean()

    fun getId(): Long
    {
        return this.id
    }

    fun getCode(): String
    {
        return this.code
    }

    fun getName(): String
    {
        return this.name
    }

    fun getFullTitle(): String
    {
        return "[" + this.countryIso + "] " + this.name
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

    fun isCustom(): Boolean
    {
        return this.custom
    }

    fun setId(id: Long)
    {
        this.id = id
    }

    fun setCountryId(countryId: Long)
    {
        this.countryId = countryId
    }

    fun setCountryName(countryName: String)
    {
        this.countryName = countryName
    }
}
