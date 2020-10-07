package com.ericg.pronotes.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.ericg.pronotes.R

class ParentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)
        // supportActionBar?.hide()
        // NavigationUI.setupActionBarWithNavController(this, findNavController(R.id.main_host_fragment))
    }
}