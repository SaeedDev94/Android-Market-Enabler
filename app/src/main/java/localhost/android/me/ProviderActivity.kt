package localhost.android.me

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import localhost.android.me.database.Callback
import localhost.android.me.database.entity.Country
import localhost.android.me.database.entity.Provider
import localhost.android.me.database.repo.CountryRepo
import localhost.android.me.database.repo.ProviderRepo
import localhost.android.me.helper.ProgressDialog

import kotlinx.android.synthetic.main.activity_provider.*

class ProviderActivity : AppCompatActivity()
{
    private var providerId: Long = 0
    private lateinit var provider: Provider
    private lateinit var progressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_provider)
        progressDialog = ProgressDialog.create(this)
        progressDialog.show()
        val intentExtra = intent.extras!!
        val mode = intentExtra.getString("mode")
        if (mode == "add")
        {
            title = "Add provider"
            edit_provider_btn.visibility = View.GONE
            add_provider_btn.visibility = View.VISIBLE
            add_provider_btn.setOnClickListener {
                addProvider()
            }
        }
        else if (mode == "edit")
        {
            title = "Edit provider"
            add_provider_btn.visibility = View.GONE
            edit_provider_btn.visibility = View.VISIBLE
            this.providerId = intentExtra.getLong("providerId")
            val countryName = intentExtra.getString("countryName")
            val providerName = intentExtra.getString("providerName")
            val providerCode = intentExtra.getString("providerCode")
            country_name.setText(countryName)
            provider_name.setText(providerName)
            provider_code.setText(providerCode)
            edit_provider_btn.setOnClickListener {
                editProvider()
            }
        }
        val repo = CountryRepo(this)
        repo.getCountries(object: Callback.OnCountryResultListener {
            override fun onResult(countries: List<Country>) {
                val data = countries.map {it.getName()}.toTypedArray()
                val adapter = ArrayAdapter<String>(applicationContext, R.layout.layout_country_arrayadapter, data)
                country_name.threshold = 1
                country_name.setAdapter(adapter)
                country_name.setDropDownBackgroundResource(android.R.color.black)
                progressDialog.dismiss()
            }
        })
    }

    private fun addProvider()
    {
        progressDialog.show()
        formValidation(object: Callback.OnDoneListener {
            override fun onSuccess() {
                insertProvider()
            }

            override fun onError(message: String) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun editProvider()
    {
        progressDialog.show()
        formValidation(object: Callback.OnDoneListener {
            override fun onSuccess() {
                updateProvider()
            }

            override fun onError(message: String) {
                progressDialog.dismiss()
                Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun formValidation(doneCallback: Callback.OnDoneListener)
    {
        val inputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
        val countryNameEditText = country_name
        val providerNameEditText = provider_name
        val providerCodeEditText = provider_code
        var countryName = countryNameEditText.text.toString()
        val providerName = providerNameEditText.text.toString()
        val providerCode = providerCodeEditText.text.toString()
        if (countryName.length < 4)
        {
            doneCallback.onError("Country name min length is 4 chars")
            return
        }
        if (providerName.length < 3)
        {
            doneCallback.onError("Provider name min length is 3 chars")
            return
        }
        if (providerCode.length < 5)
        {
            doneCallback.onError("Provider code min length is 5 chars")
            return
        }
        countryName = countryName.trim()
        countryName = countryName.substring(0, 1).toUpperCase() + countryName.substring(1)
        val repo = CountryRepo(this)
        repo.getCountryByName(countryName, object: Callback.OnCountryResultListener {
            override fun onResult(countries: List<Country>) {
                if (countries.isEmpty())
                {
                    doneCallback.onError("Country name invalid")
                    return
                }
                val country = countries[0]
                provider = Provider(country.getId(), providerCode, providerName)
                doneCallback.onSuccess()
            }
        })
    }

    private fun insertProvider()
    {
        val providerRepo = ProviderRepo(this)
        providerRepo.insertProvider(this.provider, object: Callback.OnDoneListener {
            override fun onSuccess() {
                finishThisActivity()
            }
        })
    }

    private fun updateProvider()
    {
        this.provider.setId(this.providerId)
        val providerRepo = ProviderRepo(this)
        providerRepo.updateProvider(this.provider, object: Callback.OnDoneListener {
            override fun onSuccess() {
                finishThisActivity()
            }
        })
    }

    private fun finishThisActivity()
    {
        val mainActivity = Intent(this, MainActivity::class.java)
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(mainActivity)
        finish()
    }
}
