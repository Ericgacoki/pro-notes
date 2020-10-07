package com.ericg.pronotes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.ericg.pronotes.R
import com.ericg.pronotes.model.PrefsDataStoreBooleans
import kotlinx.android.synthetic.main.splash_screen_fragment.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * @author eric
 * @date 10/2/20
 */
@Suppress("DEPRECATION")

class SplashScreen : Fragment() {
    lateinit var prefs: PrefsDataStoreBooleans

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        prefs = PrefsDataStoreBooleans(this@SplashScreen.requireContext())
        val autoSignIn = prefs.autoSignInFlow
        val showOnBoardScreen = prefs.showOnBoardFlow

     /*  fun goto(): String {
            return if (autoSignIn && !showOnBoardScreen) {
                "home viewpager"
            } else if (!autoSignIn && showOnBoardScreen) {
                "goto onboard screen"
            } else if (!autoSignIn && !showOnBoardScreen) {
                "goto sign in"
            } else "show onboard"
        }
*/
        return inflater.inflate(R.layout.splash_screen_fragment, container, false).apply {

            splashScreenText.setOnClickListener {
                findNavController().navigate(R.id.from_splashScreen_to_onBoardViewPager)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().actionBar?.hide()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDetach() {
      requireActivity().actionBar?.show()
        super.onDetach()
    }

}