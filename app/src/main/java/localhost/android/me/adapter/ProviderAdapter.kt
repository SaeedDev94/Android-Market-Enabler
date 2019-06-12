package localhost.android.me.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import localhost.android.me.model.ProviderModel
import localhost.android.me.R
import java.io.IOException

class ProviderAdapter(
    private var context: Context,
    private var providers: List<ProviderModel>
) : BaseAdapter()
{
    override fun getItem(index: Int): ProviderModel
    {
        return this.providers[index]
    }

    override fun getItemId(index: Int): Long
    {
        return index.toLong()
    }

    override fun getCount(): Int
    {
        return this.providers.size
    }

    @SuppressLint("ViewHolder")
    override fun getView(index: Int, container: View?, parent: ViewGroup): View
    {
        val view = LayoutInflater.from(this.context)
        .inflate(R.layout.layout_provider_listview, parent, false)
        val provider = getItem(index)
        val providerTextView: TextView = view.findViewById(R.id.provider_title)
        providerTextView.text = provider.getFullTitle()
        val threeDots = ContextCompat.getDrawable(this.context, R.drawable.three_dots_icon)
        threeDots?.setTint(Color.WHITE)
        try
        {
            val flagInputStream = this.context.assets.open("country_flag/"+provider.getCountryIso()+".png")
            val flag = Drawable.createFromStream(flagInputStream, null)
            flag.setBounds(0, 0, 85, 85)
            threeDots?.setBounds(0, 0, 65, 65)
            providerTextView.setCompoundDrawables(flag, null, threeDots, null)
            providerTextView.compoundDrawablePadding = 15
        }
        catch (error: IOException)
        {
            error.printStackTrace()
        }
        return view
    }
}
