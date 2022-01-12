package com.entity.unity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
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


import com.entity.unity.uibot.Chatbot
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth


class MainActivity2 : AppCompatActivity() {
    var mainActivity: MainActivity2? = null
    var isvisible = false
    private val TAG = "MainActivity2"
    private val PLAY_SERVICES_RESOLUTION_REQUEST = 9000

    private lateinit var binding: ActivityMain2Binding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        mainActivity=this
        registerWithNotificationHubs()
        FirebaseServ().createChannelAndHandleNotifications(applicationContext)
        val navView: BottomNavigationView = binding.navView
        //binding.navView.setupWithNavController(navController)
        navController = findNavController(R.id.nav_host_fragment_activity_main2)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_mentalhealth,
                R.id.navigation_counsellor,
            ), binding.container
        )
        binding.navView.setupWithNavController(navController)

        toggle = ActionBarDrawerToggle(this, binding.container, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        binding.container.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mainSidebar.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_sign_out -> {
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, ChosserActivity::class.java))
                    finish()
                }
                R.id.chatbot->{
startActivity(Intent(this, Chatbot::class.java))
                }

            }
            true
        }
        setupActionBarWithNavController(navController, appBarConfiguration)
    }
    override fun onResume() {
        super.onResume()
        isvisible = true

    }



    fun ToastNotify(notificationMessage: String?) {
        runOnUiThread {
            Toast.makeText(this@MainActivity2, notificationMessage, Toast.LENGTH_LONG).show()


        }
    }

    private fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                    .show()
            } else {
                Log.i(TAG, "This device is not supported by Google Play Services.")
                ToastNotify(

                    "This device is not supported by Google Play Services."

                )
                finish()
            }
            return false
        }
        return true
    }


    fun registerWithNotificationHubs() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with FCM.
            val intent = Intent(this, RegistrationIntentService::class.java)
            startService(intent)
        }
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

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)// toggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item)
    }*/
}