package com.ericg.pronotes.view

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.ericg.pronotes.R
import com.ericg.pronotes.model.PrefsDataStoreBooleans
import kotlinx.android.synthetic.main.splash_screen_fragment.view.*
import kotlin.properties.Delegates

/**
 * @author eric
 * @date 10/2/20
 */
@Suppress("DEPRECATION")

class SplashScreen : Fragment() {
    private var autoSignIn by Delegates.notNull<Boolean>()
    private var showOnBoardScreen by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val prefs = PrefsDataStoreBooleans(requireContext())
        val autoSignInLiveData = prefs.autoSignInFlow.asLiveData()
        val showOnBoardScreenLiveData = prefs.showOnBoardFlow.asLiveData()

        autoSignInLiveData.observe(viewLifecycleOwner, {
            autoSignIn = it
        })

        showOnBoardScreenLiveData.observe(viewLifecycleOwner, {
            showOnBoardScreen = it
        })

        fun drawable(drawable: Int) = ContextCompat.getDrawable(requireContext(), drawable)
        val images = arrayOf(
            drawable(R.drawable.cloud),
            drawable(R.drawable.notification),
            drawable(R.drawable.multi_device)
        )
        val randomIndex = (0..2).random()

        val slogan: String = when (randomIndex) {
            0 -> "Take advantage of cloud storage"
            1 -> "It has never been this great before"
            else -> "Write in one, sync in all"
        }

        fun goto() {
            return if (autoSignIn && !showOnBoardScreen) {
                findNavController().navigate(R.id.from_splashScreen_to_homeViewPager)

            } else if (!autoSignIn && showOnBoardScreen) {
                findNavController().navigate(R.id.from_splashScreen_to_onBoardViewPager)

            } else if (!autoSignIn && !showOnBoardScreen) {
                findNavController().navigate(R.id.from_splashScreen_to_signInUser)
                requireActivity().finish()
            } else
                findNavController().navigate(R.id.from_splashScreen_to_onBoardViewPager)
        }

        return inflater.inflate(R.layout.splash_screen_fragment, container, false).apply {

            this.splashScreenImage.setImageDrawable(images[randomIndex])
            this.splashScreenText.text = slogan

            Handler().postDelayed({
                goto()
            }, 3000)
        }
    }
}