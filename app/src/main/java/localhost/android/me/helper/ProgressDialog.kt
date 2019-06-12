package localhost.android.me.helper

import android.app.Activity
import android.app.Dialog
import android.view.LayoutInflater
import android.widget.RelativeLayout
import localhost.android.me.R

class ProgressDialog
{
    companion object {
        fun create(activity: Activity): Dialog
        {
            val dialog = Dialog(activity)
            val view = LayoutInflater.from(activity).inflate(R.layout.layout_loading_dialog, RelativeLayout(activity), true)
            dialog.setContentView(view)
            dialog.setCancelable(false)
            dialog.create()
            return dialog
        }
    }
}
