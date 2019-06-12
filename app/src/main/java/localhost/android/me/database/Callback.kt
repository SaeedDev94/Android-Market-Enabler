package localhost.android.me.database

import localhost.android.me.database.entity.Country
import localhost.android.me.database.model.ProviderWithCountry

interface Callback
{
    interface OnDoneListener
    {
        fun onSuccess()

        fun onError(message: String) {}
    }

    interface OnCountryResultListener
    {
        fun onResult(countries: List<Country>)
    }

    interface OnProviderResultListener
    {
        fun onResult(providers: List<ProviderWithCountry>)
    }
}
