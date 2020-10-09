package com.ericg.pronotes.extentions

import android.view.Gravity
import android.widget.Toast
import androidx.fragment.app.Fragment

/**
 * @author eric
 * @date 10/8/20
 */
object Extensions {
    fun Fragment.toast(msg: String): Unit {
        return Toast.makeText(this.requireContext(), msg, Toast.LENGTH_LONG).apply {
            setGravity(Gravity.CENTER_VERTICAL, 0, 0)
        }.show()
    }
}