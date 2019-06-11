package localhost.android.me.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.List;
import localhost.android.me.helper.AssetHelper;
import localhost.android.me.model.ProviderModel;
import localhost.android.me.R;

public class ProviderAdapter extends BaseAdapter
{
    private Context context;
    private List<ProviderModel> providers;

    public ProviderAdapter(Context context, List<ProviderModel> providers)
    {
        this.context = context;
        this.providers = providers;
    }

    @Override
    public ProviderModel getItem(int index)
    {
        return this.providers.get(index);
    }

    @Override
    public long getItemId(int index)
    {
        return index;
    }

    @Override
    public int getCount()
    {
        return this.providers.size();
    }

    @Override
    public View getView(int index, View view, ViewGroup parent)
    {
        if (view == null)
        {
            view = LayoutInflater.from(this.context)
            .inflate(R.layout.providers_listview, parent, false);
        }
        ProviderModel provider = getItem(index);
        TextView providerTextView = view.findViewById(R.id.provider_title);
        providerTextView.setText(provider.getFullTitle());
        AssetHelper flag = new AssetHelper(this.context, "country_flag/"+provider.getCountryIso()+".png");
        Drawable threeDots = this.context.getResources().getDrawable(R.drawable.three_dots_icon);
        threeDots.setTint(Color.WHITE);
        if (flag.setDrawable())
        {
            Drawable flagImg = flag.getDrawable();
            flagImg.setBounds(0, 0, 85, 85);
            threeDots.setBounds(0, 0, 65, 65);
            providerTextView.setCompoundDrawables(flagImg, null, threeDots, null);
            providerTextView.setCompoundDrawablePadding(15);
        }
        return view;
    }
}
