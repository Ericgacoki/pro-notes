package com.ericg.pronotes.view

import android.os.Bundle
import android.os.Handler
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ericg.pronotes.R

class ParentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_parent)
        // supportActionBar?.hide()
        // NavigationUI.setupActionBarWithNavController(this, findNavController(R.id.main_host_fragment))

     //   supportActionBar?.title = if (homeViewPager2?.currentItem == 0) "Notes" else "Todo"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                // todo check if this works
                // findNavController().navigate(R.id.from_homeViewPager_to_settings)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    var canGoBack = false

    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        if (canGoBack) {
            super.onBackPressed()
        } else {
            canGoBack = true
            Toast.makeText(this, "press again to exit", Toast.LENGTH_SHORT).apply {
                setGravity(Gravity.CENTER, 0, 0)
            }.show()
            Handler().postDelayed({ canGoBack = false }, 2000)
        }
    }
}