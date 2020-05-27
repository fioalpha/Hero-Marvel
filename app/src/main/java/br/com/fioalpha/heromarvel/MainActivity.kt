package br.com.fioalpha.heromarvel

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val bottomNavigation: BottomNavigationView by lazy {
        findViewById<BottomNavigationView>(R.id.bottom_navigation)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNavigation(setupToolbar())
    }

    private fun setupNavigation(toolbar: Toolbar) {
        val navController = findNavController(R.id.host_nav)
        bottomNavigation.setupWithNavController(navController)
        val appConfiguration = AppBarConfiguration.Builder(navController.graph).build()
        NavigationUI.setupWithNavController(toolbar, navController, appConfiguration)
    }

    private fun setupToolbar(): Toolbar {
        return findViewById<Toolbar>(R.id.toolbar).apply {
            setSupportActionBar(this)
        }
    }

}
