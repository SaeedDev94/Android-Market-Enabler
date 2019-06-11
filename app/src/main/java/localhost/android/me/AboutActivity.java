package localhost.android.me;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import localhost.android.me.helper.AssetHelper;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_layout);
        AssetHelper about = new AssetHelper(this, "html_page/about.html");
        if (about.setString()) {
            TextView aboutContent = findViewById(R.id.about_content);
            aboutContent.setText(
                    Html.fromHtml(about.getString(), Html.FROM_HTML_MODE_LEGACY)
            );
            aboutContent.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
}
