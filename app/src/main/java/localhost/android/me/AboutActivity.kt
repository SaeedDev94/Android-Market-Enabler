package localhost.android.me

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Html
import android.text.method.LinkMovementMethod

import kotlinx.android.synthetic.main.activity_about.*

class AboutActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val about = resources.assets.open("html_page/about.html").bufferedReader().use { it.readText() }
        about_content.text = Html.fromHtml(about, Html.FROM_HTML_MODE_LEGACY)
        about_content.movementMethod = LinkMovementMethod.getInstance()
    }
}
