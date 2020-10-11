package com.ericg.pronotes.onboardviews

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.ericg.pronotes.R
import com.ericg.pronotes.model.PrefsBooleanType
import com.ericg.pronotes.model.PrefsDataStoreBooleans
import kotlinx.android.synthetic.main.onboard3_fragment.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author eric
 * @date 10/2/20
 */

class OnBoard3 : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val onBoardViewPager = activity?.findViewById<ViewPager2>(R.id.onboardViewPager2)
        val view = inflater.inflate(R.layout.onboard3_fragment, container, false)

        return view.apply {
            this.dot1_onboard3.setOnClickListener {
                onBoardViewPager?.currentItem = 0
            }
            dot2_onboard3.setOnClickListener {
                onBoardViewPager?.currentItem = 1
            }
            btn_finish_onboard.setOnClickListener {
                GlobalScope.launch(Dispatchers.IO) {
                    PrefsDataStoreBooleans(requireContext()).setPrefsBoolean(
                        PrefsBooleanType.SHOW_ONBOARD_SCREEN, false
                    )
                }

                var autoSignIn: Boolean = false
                PrefsDataStoreBooleans(requireContext()).autoSignInFlow.asLiveData()
                    .observe(viewLifecycleOwner, {
                        autoSignIn = it
                    })

                if (!autoSignIn) {
                    findNavController().navigate(R.id.from_onBoardViewPager_to_signInUser)
                    requireActivity().finish()
                } else {
                    findNavController().navigate(R.id.from_onBoardViewPager_to_homeViewPager)
                }
            }
        }
    }
}