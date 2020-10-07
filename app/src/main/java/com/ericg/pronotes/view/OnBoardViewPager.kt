package com.ericg.pronotes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ericg.pronotes.R
import com.ericg.pronotes.adapters.OnBoardViewPagerAdapter
import com.ericg.pronotes.onboardviews.OnBoard1
import com.ericg.pronotes.onboardviews.OnBoard2
import com.ericg.pronotes.onboardviews.OnBoard3
import kotlinx.android.synthetic.main.onboard_view_pager2_fragment.view.*

/**
 * @author eric
 * @date 10/2/20
 */

class OnBoardViewPager : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentList: ArrayList<Fragment> = arrayListOf(OnBoard1(), OnBoard2(), OnBoard3())

        val adapter = OnBoardViewPagerAdapter(
            requireActivity().supportFragmentManager,
            lifecycle,
            fragmentList
        )

        return inflater.inflate(R.layout.onboard_view_pager2_fragment, container, false).apply {
            onboardViewPager2.adapter = adapter
        }
    }
}