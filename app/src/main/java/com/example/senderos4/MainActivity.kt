package com.example.senderos4


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    val fragment = supportFragmentManager.findFragmentById(R.id.map_fragment)
    private lateinit var map: GoogleMap

    private lateinit var toolbar: Toolbar
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationVIew: NavigationView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    //private lateinit var modGuestImage: LinearLayout
    private lateinit var settingBottom: ImageView
    private lateinit var loginTextView: TextView


    val app by lazy {
        application as SenderosApplication
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        splashScreen.setKeepOnScreenCondition{false}
        bind()
        navSetting()
        setSupportActionBar(toolbar)
        appBarConfiguration = AppBarConfiguration((navController.graph), drawerLayout)
        setupActionBarWithNavController(navController, drawerLayout)
        navigationVIew.setupWithNavController(navController)

        //nuevo
        updateNavigationViewHeader()
    }



    private fun navSetting() {

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
        settingBottom = headerView.findViewById(R.id.setting_options)
        loginTextView = headerView.findViewById(R.id.textHeader)
    }


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainerView)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }


    // boton inicio de sesion falta completar
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar, menu)
        return true
    }

    //new
    fun updateNavigationViewHeader() {
        if (app.isLoggedIn()) {
            loginTextView.text = "Logout"
            loginTextView.setOnClickListener {
                app.clearAuthToken()
                updateNavigationViewHeader()

                drawerLayout.closeDrawer(GravityCompat.START)
            }
        } else {
            loginTextView.text = "Login"
            loginTextView.setOnClickListener {
                navController.navigate(R.id.action_map_fragment_to_loginFragment)
                drawerLayout.closeDrawer(GravityCompat.START)
            }
        }
    }
}

