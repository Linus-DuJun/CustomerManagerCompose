package org.linus.core.utils.toast

import androidx.annotation.StringRes

interface Toaster {
    fun showToast(msg: String)

    fun showToast(@StringRes id: Int, vararg args: Any)
}