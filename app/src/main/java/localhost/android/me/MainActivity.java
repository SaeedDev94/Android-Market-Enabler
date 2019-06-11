package localhost.android.me;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDelegate;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import com.jaredrummler.android.shell.Shell;
import java.util.ArrayList;
import java.util.List;
import localhost.android.me.database.entity.Provider;
import localhost.android.me.database.model.ProviderWithCountry;
import localhost.android.me.database.repo.ProviderRepo;
import localhost.android.me.adapter.ProviderAdapter;
import localhost.android.me.model.ProviderModel;

public class MainActivity extends AppCompatActivity
{
    static
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
    }

    private List<ProviderModel> providers = new ArrayList<>();
    private ListView providersList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        ProviderRepo providerRepo = new ProviderRepo(this);
        List<ProviderWithCountry> customProviders = providerRepo.getProviders();
        for (ProviderWithCountry provider : customProviders)
        {
            ProviderModel providerModel = new ProviderModel(provider.getProviderCode(), provider.getCountryIso(), provider.getProviderName(), "true");
            providerModel.setId(provider.getProviderId());
            providerModel.setCountryId(provider.getCountryId());
            providerModel.setCountryName(provider.getCountryName());
            providers.add(providerModel);
        }
        String[][] mainProviders = new String[][]
        {
            {"310260", "US", "T-Mobile", "false"},
            {"302690", "CA", "Bell Mobility", "false"},
            {"302720", "CA", "Rogers Wireless", "false"},
            {"23407", "GB", "Vodafone", "false"},
            {"23420", "GB", "Three", "false"},
            {"20416", "NL", "T-Mobile", "false"},
            {"23203", "AU", "T-Mobile", "false"},
            {"26207", "DE", "O2", "false"},
            {"26203", "DE", "E-Plus", "false"},
            {"22802", "CH", "Sunrise", "false"},
            {"22201", "IT", "TIM", "false"},
            {"40405", "IN", "Vodafone", "false"},
            {"40402", "IN", "AirTel", "false"},
            {"45502", "CN", "China Telecom", "false"},
            {"27203", "IE", "Meteor", "false"},
            {"25001", "RU", "MTS", "false"},
            {"25002", "RU", "MegaFon", "false"},
            {"25099", "RU", "Beeline", "false"},
            {"25020", "RU", "Tele2", "false"}
        };
        for (String[] provider : mainProviders)
        {
            providers.add(new ProviderModel(provider[0], provider[1], provider[2], provider[3]));
        }
        ProviderAdapter adapter = new ProviderAdapter(this, providers);
        providersList = findViewById(R.id.providers_list);
        providersList.setAdapter(adapter);
        providersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int index, long id)
            {
                listItemOptions(providers.get(index), view);
            }
        });
        FloatingActionButton btn = findViewById(R.id.fab);
        btn.setColorFilter(Color.WHITE);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent provider = new Intent(getApplicationContext(), ProviderActivity.class);
                provider.putExtra("mode", "add");
                startActivity(provider);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == R.id.about_item)
        {
            Intent about = new Intent(this, AboutActivity.class);
            startActivity(about);
        }
        return super.onOptionsItemSelected(item);
    }

    public void listItemOptions(ProviderModel provider, View view)
    {
        PopupMenu popupMenu = new PopupMenu(this, view, Gravity.END);
        popupMenu.inflate(R.menu.list_item_options);
        if (provider.isCustom())
        {
            popupMenu.getMenu().setGroupVisible(R.id.group_custom_list_options, true);
        }
        else
        {
            popupMenu.getMenu().setGroupVisible(R.id.group_custom_list_options, false);
        }
        popupMenu.setOnMenuItemClickListener(new PopMenuHandler(this, provider));
        popupMenu.show();
    }

    public boolean fakeProvider(ProviderModel provider)
    {
        if (!Shell.SU.available())
        {
            return false;
        }
        Shell.SU.run("setprop gsm.sim.operator.numeric "+provider.getCode());
        Shell.SU.run("killall com.android.vending");
        Shell.SU.run("am force-stop com.android.vending");
        Shell.SU.run("rm -rf /data/data/com.android.vending/cache/*");
        return true;
    }

    public class PopMenuHandler implements PopupMenu.OnMenuItemClickListener
    {
        private Context context;
        private ProviderModel provider;

        PopMenuHandler(Context context, ProviderModel provider)
        {
            this.context = context;
            this.provider = provider;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item)
        {
            if (item.getItemId() == R.id.fake_provider)
            {
                String result = this.provider.getFullTitle();
                if (!fakeProvider(this.provider))
                {
                    result = "This app needs root access!!";
                }
                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                return true;
            }
            if (item.getItemId() == R.id.edit_provider)
            {
                Intent provider = new Intent(getApplicationContext(), ProviderActivity.class);
                provider.putExtra("mode", "edit");
                provider.putExtra("providerId", String.valueOf(this.provider.getId()));
                provider.putExtra("countryName", this.provider.getCountryName());
                provider.putExtra("providerName", this.provider.getName());
                provider.putExtra("providerCode", this.provider.getCode());
                startActivity(provider);
                return true;
            }
            if (item.getItemId() == R.id.delete_provider)
            {
                Provider provider = new Provider(this.provider.getCountryId(), this.provider.getCode(), this.provider.getName());
                provider.setId(this.provider.getId());
                ProviderRepo providerRepo = new ProviderRepo(this.context);
                providerRepo.deleteProvider(provider);
                int providerIndex = providers.indexOf(this.provider);
                providers.remove(providerIndex);
                providersList.invalidateViews();
                return true;
            }
            return false;
        }
    }
}
