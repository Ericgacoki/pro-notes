package com.ericg.pronotes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ericg.pronotes.R
import com.ericg.pronotes.adapters.HomeViewPagerAdapter
import kotlinx.android.synthetic.main.home_view_pager2.view.*

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
        val homeFragments = listOf<Fragment>(ProNotes(), Todo())
        val adapter = HomeViewPagerAdapter(requireActivity().supportFragmentManager, lifecycle, homeFragments)

        return inflater.inflate(R.layout.home_view_pager2, container, false).also {
           it.homeViewPager2.adapter = adapter
/*
            if (it.homeViewPager2.currentItem == 0){
                toast("pro notes")
                requireActivity().actionBar?.title = "Pro Notes"
            } else {
                toast("todo")
                requireActivity().actionBar?.title = "Todo"
            }*/
        }
    }
}