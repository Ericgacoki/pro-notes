package com.ericg.pronotes.firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ericg.pronotes.R

/**
 * @author eric
 * @date 10/2/20
 */

class CreateAccount: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.create_account_fragment, container, false)

        return view
    }
}