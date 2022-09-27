package org.linus.core.utils.toast

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.annotation.StringRes

class SimpleToaster constructor(private val context: Context) : Toaster {

    override fun showToast(@StringRes id: Int, vararg args: Any) {
        Handler(Looper.getMainLooper()).post {
            Toast.makeText(context, context.getString(id, *args), Toast.LENGTH_SHORT).show()
        }
    }

    override fun showToast(msg: String) {
        Handler(Looper.getMainLooper()).post { Toast.makeText(context, msg, Toast.LENGTH_SHORT).show() }
    }
}