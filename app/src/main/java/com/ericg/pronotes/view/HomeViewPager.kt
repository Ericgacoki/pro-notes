package com.ericg.pronotes.view

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
class HomeViewPager: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.home_view_pager2, container, false)

        return view
    }
}