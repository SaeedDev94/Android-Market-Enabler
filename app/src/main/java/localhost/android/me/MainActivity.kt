package localhost.android.me

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.PopupMenu
import android.widget.Toast
import com.jaredrummler.android.shell.Shell
import java.util.ArrayList
import localhost.android.me.database.entity.Provider
import localhost.android.me.database.repo.ProviderRepo
import localhost.android.me.adapter.ProviderAdapter
import localhost.android.me.model.ProviderModel

import kotlinx.android.synthetic.main.main_layout.*

class MainActivity : AppCompatActivity()
{
    companion object {
        init {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }

        fun fakeProvider(provider: ProviderModel): Boolean
        {
            if (!Shell.SU.available())
            {
                return false
            }
            Shell.SU.run("setprop gsm.sim.operator.numeric "+provider.getCode())
            Shell.SU.run("killall com.android.vending")
            Shell.SU.run("am force-stop com.android.vending")
            Shell.SU.run("rm -rf /data/data/com.android.vending/cache/*")
            return true
        }
    }

    private var providers: ArrayList<ProviderModel> = arrayListOf()
    private lateinit var providersList: ListView

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout)
        val providerRepo = ProviderRepo(this)
        val customProviders = providerRepo.getProviders()
        for (i in 0 until customProviders.size)
        {
            val provider = customProviders[i]
            val providerModel = ProviderModel(provider.getProviderCode(), provider.getCountryIso(), provider.getProviderName(), "true")
            providerModel.setId(provider.getProviderId())
            providerModel.setCountryId(provider.getCountryId())
            providerModel.setCountryName(provider.getCountryName())
            providers.add(providerModel)
        }
        val mainProviders = arrayOf(
            arrayOf("310260", "US", "T-Mobile"),
            arrayOf("302690", "CA", "Bell Mobility"),
            arrayOf("302720", "CA", "Rogers Wireless"),
            arrayOf("23407", "GB", "Vodafone"),
            arrayOf("23420", "GB", "Three"),
            arrayOf("20416", "NL", "T-Mobile"),
            arrayOf("23203", "AU", "T-Mobile"),
            arrayOf("26207", "DE", "O2"),
            arrayOf("26203", "DE", "E-Plus"),
            arrayOf("22802", "CH", "Sunrise"),
            arrayOf("22201", "IT", "TIM"),
            arrayOf("40405", "IN", "Vodafone"),
            arrayOf("40402", "IN", "AirTel"),
            arrayOf("45502", "CN", "China Telecom"),
            arrayOf("27203", "IE", "Meteor"),
            arrayOf("25001", "RU", "MTS"),
            arrayOf("25002", "RU", "MegaFon"),
            arrayOf("25099", "RU", "Beeline"),
            arrayOf("25020", "RU", "Tele2")
        )
        mainProviders.forEach {
            providers.add(ProviderModel(it[0], it[1], it[2], "false"))
        }
        providersList = providers_list
        providersList.adapter = ProviderAdapter(this, providers)
        providersList.setOnItemClickListener { _, view, index, _ ->
            listItemOptions(providers[index], view)
        }
        fab.setColorFilter(Color.WHITE)
        fab.setOnClickListener {
            val providerActivity = Intent(applicationContext, ProviderActivity::class.java)
            providerActivity.putExtra("mode", "add")
            startActivity(providerActivity)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean
    {
        menuInflater.inflate(R.menu.menu_items, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == R.id.about_item)
        {
            val aboutActivity = Intent(this, AboutActivity::class.java)
            startActivity(aboutActivity)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun listItemOptions(provider: ProviderModel, view: View)
    {
        val popupMenu = PopupMenu(this, view, Gravity.END)
        popupMenu.inflate(R.menu.list_item_options)
        if (provider.isCustom())
        {
            popupMenu.menu.setGroupVisible(R.id.group_custom_list_options, true)
        }
        else
        {
            popupMenu.menu.setGroupVisible(R.id.group_custom_list_options, false)
        }
        popupMenu.setOnMenuItemClickListener(PopMenuHandler(this, provider))
        popupMenu.show()
    }

    class PopMenuHandler(
        private var activity: MainActivity,
        private var provider: ProviderModel
    ) : PopupMenu.OnMenuItemClickListener
    {
        override fun onMenuItemClick(item: MenuItem): Boolean
        {
            if (item.itemId == R.id.fake_provider)
            {
                var result = this.provider.getFullTitle()
                if (!fakeProvider(this.provider))
                {
                    result = "This app needs root access!!"
                }
                Toast.makeText(this.activity, result, Toast.LENGTH_SHORT).show()
                return true
            }
            if (item.itemId == R.id.edit_provider)
            {
                val providerActivity = Intent(this.activity, ProviderActivity::class.java)
                providerActivity.putExtra("mode", "edit")
                providerActivity.putExtra("providerId", this.provider.getId().toString())
                providerActivity.putExtra("countryName", this.provider.getCountryName())
                providerActivity.putExtra("providerName", this.provider.getName())
                providerActivity.putExtra("providerCode", this.provider.getCode())
                this.activity.startActivity(providerActivity)
                return true
            }
            if (item.itemId == R.id.delete_provider)
            {
                val provider = Provider(this.provider.getCountryId(), this.provider.getCode(), this.provider.getName())
                provider.setId(this.provider.getId())
                val providerRepo = ProviderRepo(this.activity.applicationContext)
                providerRepo.deleteProvider(provider)
                val providerIndex = this.activity.providers.indexOf(this.provider)
                this.activity.providers.removeAt(providerIndex)
                this.activity.providersList.invalidateViews()
                return true
            }
            return false
        }
    }
}
