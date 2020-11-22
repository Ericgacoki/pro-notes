package com.ericg.pronotes.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ericg.pronotes.R

class Settings : Fragment() {
    lateinit var settingsLay: View
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        handleClicks()
        settingsLay = inflater.inflate(R.layout.settings, container)
        return settingsLay
    }

    private fun handleClicks() {
        settingsLay.apply {
            // todo access view here

            // todo use dataStore for some values
        }
    }
}