package com.example.senderos4

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint.Style
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.navigation.NavigationView
import java.util.prefs.Preferences

class MainActivity : AppCompatActivity() {

    val fragment = supportFragmentManager.findFragmentById(R.id.map_fragment)
    private lateinit var map: GoogleMap

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationVIew: NavigationView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var loginBottom: ImageView
    private lateinit var settingBottom: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bind()
        addListeners()

        setSupportActionBar(toolbar)
        appBarConfiguration = AppBarConfiguration((navController.graph), drawerLayout)
        setupActionBarWithNavController(navController, drawerLayout)
        navigationVIew.setupWithNavController(navController)

       // toolbar.setTitleTextColor(Color.TRANSPARENT)

    }

    private fun addListeners() {
        loginBottom.setOnClickListener {
            navController.navigate(R.id.action_home_Fragment_to_clasifications_Fragment) //id del fragmento hacia donde nos va a mover
            drawerLayout.closeDrawer(GravityCompat.START)
        }

        settingBottom.setOnClickListener {
            navController.navigate(R.id.action_map_fragment_to_settingsFragment)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

  
    }

    fun bind() {
        toolbar = findViewById(R.id.myToolbar)
        drawerLayout = findViewById(R.id.drawer)
        navController = findNavController(R.id.fragmentContainerView)
        navigationVIew = findViewById(R.id.navigationView)

        val headerView = navigationVIew.getHeaderView(0)

        loginBottom = headerView.findViewById(R.id.login)
        settingBottom = headerView.findViewById(R.id.setting_options)

    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }





    /*     drawerLayout.setOnClickListener {
             drawerLayout.open()

         }
         drawerLayout.close() */

}