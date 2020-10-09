package com.ericg.pronotes.view

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ericg.pronotes.R

class ParentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)
        // supportActionBar?.hide()
        // NavigationUI.setupActionBarWithNavController(this, findNavController(R.id.main_host_fragment))
    }

    var canGoBack = false

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (canGoBack) {
            super.onBackPressed()
        } else {
            canGoBack = true
            Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER,0,0)
            }.show()
            Handler().postDelayed({ canGoBack = false }, 2000)
        }
    }
}