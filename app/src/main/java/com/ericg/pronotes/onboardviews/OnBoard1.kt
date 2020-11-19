package com.ericg.pronotes.onboardviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ericg.pronotes.R
import kotlinx.android.synthetic.main.onboard1_fragment.view.*

/**
 * @author eric
 * @date 10/2/20
 */

class OnBoard1 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val onBoardViewPager = activity?.findViewById<ViewPager2>(R.id.onboardViewPager2)
        val view = inflater.inflate(R.layout.onboard1_fragment, container, false)

        return view.apply {
            this.dot2_onboard1.setOnClickListener {
                onBoardViewPager?.currentItem = 1
            }
            dot3_onboard1.setOnClickListener {
                onBoardViewPager?.currentItem = 2
            }
        }
    }
}