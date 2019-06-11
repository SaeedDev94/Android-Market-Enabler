package localhost.android.me;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.List;
import room.entity.Country;
import room.entity.Provider;
import room.repo.CountryRepo;
import room.repo.ProviderRepo;

public class ProviderActivity extends AppCompatActivity
{
    private Provider provider;
    private long providerId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.provider_layout);
        Button addProviderBtn = findViewById(R.id.add_provider_btn);
        Button editProviderBtn = findViewById(R.id.edit_provider_btn);
        Intent intent = getIntent();
        String mode = intent.getStringExtra("mode");
        if (mode.equals("add"))
        {
            setTitle("Add provider");
            editProviderBtn.setVisibility(View.GONE);
            addProviderBtn.setVisibility(View.VISIBLE);
        }
        else if (mode.equals("edit"))
        {
            setTitle("Edit provider");
            addProviderBtn.setVisibility(View.GONE);
            editProviderBtn.setVisibility(View.VISIBLE);
            this.providerId = Long.parseLong(intent.getStringExtra("providerId"));
            String countryName = intent.getStringExtra("countryName");
            String providerName = intent.getStringExtra("providerName");
            String providerCode = intent.getStringExtra("providerCode");
            AutoCompleteTextView countryNameEditText = findViewById(R.id.country_name);
            EditText providerNameEditText = findViewById(R.id.provider_name);
            EditText providerCodeEditText = findViewById(R.id.provider_code);
            countryNameEditText.setText(countryName);
            providerNameEditText.setText(providerName);
            providerCodeEditText.setText(providerCode);
        }
        try
        {
            CountryRepo repo = new CountryRepo(this);
            List<Country> countriesList = repo.getCountries();
            int countriesLen = countriesList.size();
            String[] countries = new String[countriesLen];
            for (int i = 0 ; i < countriesLen ; i++)
            {
                countries[i] = countriesList.get(i).getName();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.select_dialog_item, countries);
            AutoCompleteTextView countryNameEditText = findViewById(R.id.country_name);
            countryNameEditText.setThreshold(1);
            countryNameEditText.setAdapter(adapter);
        }
        catch (InterruptedException error)
        {
            error.printStackTrace();
            Toast.makeText(this, "Somethings went wrong!!2", Toast.LENGTH_SHORT).show();
        }
    }

    public void addProvider(View view)
    {
        ProgressDialog progressDialog = ProgressDialog.show(ProviderActivity.this, "", "Loading...", true);
        if (formValidationFailed(progressDialog))
        {
            return;
        }
        try
        {
            ProviderRepo providerRepo = new ProviderRepo(this);
            providerRepo.insertProvider(provider);
        }
        catch (InterruptedException error)
        {
            error.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, "Somethings went wrong!!4", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent main = new Intent(this, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
    }

    public void editProvider(View view)
    {
        ProgressDialog progressDialog = ProgressDialog.show(ProviderActivity.this, "", "Loading...", true);
        if (formValidationFailed(progressDialog))
        {
            return;
        }
        this.provider.setId(this.providerId);
        try
        {
            ProviderRepo providerRepo = new ProviderRepo(this);
            providerRepo.updateProvider(this.provider);
        }
        catch (InterruptedException error)
        {
            error.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, "Somethings went wrong!!5", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent main = new Intent(this, MainActivity.class);
        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(main);
    }

    public boolean formValidationFailed(ProgressDialog progressDialog)
    {
        AutoCompleteTextView countryNameEditText = findViewById(R.id.country_name);
        EditText providerNameEditText = findViewById(R.id.provider_name);
        EditText providerCodeEditText = findViewById(R.id.provider_code);
        String countryName = countryNameEditText.getText().toString();
        String providerName = providerNameEditText.getText().toString();
        String providerCode = providerCodeEditText.getText().toString();
        if (countryName.length() < 4)
        {
            progressDialog.dismiss();
            Toast.makeText(this, "Country name min length is 4 chars", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (providerName.length() < 3)
        {
            progressDialog.dismiss();
            Toast.makeText(this, "Provider name min length is 3 chars", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (providerCode.length() < 5)
        {
            progressDialog.dismiss();
            Toast.makeText(this, "Provider code min length is 5 chars", Toast.LENGTH_SHORT).show();
            return true;
        }
        countryName = countryName.trim();
        countryName = countryName.substring(0, 1).toUpperCase() + countryName.substring(1);
        List<Country> countryList;
        try
        {
            CountryRepo repo = new CountryRepo(this);
            countryList = repo.getCountryByName(countryName);
            if (countryList.size() == 0)
            {
                progressDialog.dismiss();
                Toast.makeText(this, "Country name invalid", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        catch (InterruptedException error)
        {
            error.printStackTrace();
            progressDialog.dismiss();
            Toast.makeText(this, "Somethings went wrong!!3", Toast.LENGTH_SHORT).show();
            return true;
        }
        Country country = countryList.get(0);
        this.provider = new Provider(country.getId(), providerCode, providerName);
        return false;
    }
}
