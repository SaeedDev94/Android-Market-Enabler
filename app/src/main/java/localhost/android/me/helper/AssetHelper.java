package localhost.android.me.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetHelper
{
    private Context context;
    private String assetPath;
    private String string;
    private Drawable drawable;

    public AssetHelper(Context context, String assetPath)
    {
        this.context = context;
        this.assetPath = assetPath;
    }

    public boolean setString()
    {
        try
        {
            InputStream inputStream = this.context.getResources().getAssets().open(this.assetPath);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(line).append("\n");
            }
            this.string = stringBuilder.toString();
            return true;
        }
        catch (IOException error)
        {
            error.printStackTrace();
            return false;
        }
    }

    public boolean setDrawable()
    {
        try
        {
            InputStream inputStream = this.context.getResources().getAssets().open(this.assetPath);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            this.drawable = new BitmapDrawable(bitmap);
            return true;
        }
        catch (IOException error)
        {
            error.printStackTrace();
            return false;
        }
    }

    public String getString()
    {
        return this.string;
    }

    public Drawable getDrawable()
    {
        return this.drawable;
    }
}
