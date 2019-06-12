package localhost.android.me

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import localhost.android.me.database.entity.Provider
import localhost.android.me.database.repo.CountryRepo
import localhost.android.me.database.repo.ProviderRepo
import localhost.android.me.helper.ProgressDialog

import kotlinx.android.synthetic.main.provider_layout.*

class ProviderActivity : AppCompatActivity()
{
    private var providerId: Long = 0
    private lateinit var provider: Provider

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.provider_layout)
        val addProviderBtn = add_provider_btn
        val editProviderBtn = edit_provider_btn
        val intentExtra = intent.extras!!
        val mode = intentExtra.getString("mode")
        if (mode == "add")
        {
            title = "Add provider"
            editProviderBtn.visibility = View.GONE
            addProviderBtn.visibility = View.VISIBLE
            addProviderBtn.setOnClickListener {
                addProvider()
            }
        }
        else if (mode == "edit")
        {
            title = "Edit provider"
            addProviderBtn.visibility = View.GONE
            editProviderBtn.visibility = View.VISIBLE
            this.providerId = intentExtra.getString("providerId")?.toLong()!!
            val countryName = intentExtra.getString("countryName")
            val providerName = intentExtra.getString("providerName")
            val providerCode = intentExtra.getString("providerCode")
            val countryNameEditText = country_name
            val providerNameEditText = provider_name
            val providerCodeEditText = provider_code
            countryNameEditText.setText(countryName)
            providerNameEditText.setText(providerName)
            providerCodeEditText.setText(providerCode)
            editProviderBtn.setOnClickListener {
                editProvider()
            }
        }
        val repo = CountryRepo(this)
        val countriesList = repo.getCountries()
        val countries: ArrayList<String> = arrayListOf()
        for (i in 0 until countriesList.size)
        {
            countries.add(countriesList[i].getName())
        }
        val adapter = ArrayAdapter<String>(this, android.R.layout.select_dialog_item, countries)
        val countryNameEditText = country_name
        countryNameEditText.threshold = 1
        countryNameEditText.setAdapter(adapter)
    }

    private fun addProvider()
    {
        val progressDialog = ProgressDialog.create(this)
        progressDialog.show()
        if (formValidationFailed(progressDialog))
        {
            return
        }
        val providerRepo = ProviderRepo(this)
        providerRepo.insertProvider(this.provider)
        val mainActivity = Intent(this, MainActivity::class.java)
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(mainActivity)
    }

    private fun editProvider()
    {
        val progressDialog = ProgressDialog.create(this)
        progressDialog.show()
        if (formValidationFailed(progressDialog))
        {
            return
        }
        this.provider.setId(this.providerId)
        val providerRepo = ProviderRepo(this)
        providerRepo.updateProvider(this.provider)
        val mainActivity = Intent(this, MainActivity::class.java)
        mainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(mainActivity)
    }

    private fun formValidationFailed(progressDialog: Dialog): Boolean
    {
        val countryNameEditText = country_name
        val providerNameEditText = provider_name
        val providerCodeEditText = provider_code
        var countryName = countryNameEditText.text.toString()
        val providerName = providerNameEditText.text.toString()
        val providerCode = providerCodeEditText.text.toString()
        if (countryName.length < 4)
        {
            progressDialog.dismiss()
            Toast.makeText(this, "Country name min length is 4 chars", Toast.LENGTH_SHORT).show()
            return true
        }
        if (providerName.length < 3)
        {
            progressDialog.dismiss()
            Toast.makeText(this, "Provider name min length is 3 chars", Toast.LENGTH_SHORT).show()
            return true
        }
        if (providerCode.length < 5)
        {
            progressDialog.dismiss()
            Toast.makeText(this, "Provider code min length is 5 chars", Toast.LENGTH_SHORT).show()
            return true
        }
        countryName = countryName.trim()
        countryName = countryName.substring(0, 1).toUpperCase() + countryName.substring(1)
        val repo = CountryRepo(this)
        val countryList = repo.getCountryByName(countryName)
        if (countryList.isEmpty())
        {
            progressDialog.dismiss()
            Toast.makeText(this, "Country name invalid", Toast.LENGTH_SHORT).show()
            return true
        }
        val country = countryList[0]
        this.provider = Provider(country.getId(), providerCode, providerName)
        progressDialog.dismiss()
        return false
    }
}
