package com.entity.unity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.entity.unity.databinding.ActivityMain2Binding
import com.google.firebase.auth.FirebaseAuth


class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView
        //binding.navView.setupWithNavController(navController)
        navController = findNavController(R.id.nav_host_fragment_activity_main2)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_mentalhealth,
                R.id.navigation_exercise,
            ), binding.container
        )
        binding.navView.setupWithNavController(navController)



        //
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_activity_main2).navigateUp(
            appBarConfiguration!!
        )
    }

    override fun onBackPressed() {

        if (binding.container.isDrawerOpen(GravityCompat.START)) {

            binding.container.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}