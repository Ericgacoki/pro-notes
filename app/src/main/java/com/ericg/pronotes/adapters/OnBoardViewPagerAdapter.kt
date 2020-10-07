package com.ericg.pronotes.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author eric
 * @date 10/2/20
 */

class OnBoardViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    fragments: ArrayList<Fragment>
) : FragmentStateAdapter(
    fragmentManager, lifecycle
) {
    private val fragmentsList = fragments

    override fun getItemCount(): Int {
        return fragmentsList.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragmentsList[position]
    }
}