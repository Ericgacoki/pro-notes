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
class ProNotes: Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.pro_notes_fragment, container, false).apply {

        }
    }
}