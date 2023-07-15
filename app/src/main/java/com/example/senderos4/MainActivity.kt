package com.example.senderos4


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var userName: TextView
    private lateinit var noWifi :ImageView

    private lateinit var connectivityManager: ConnectivityManager
    private lateinit var connectivityReceiver: ConnectivityReceiver

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

        connectionInternet()
        observeLoginData()
    }

    private fun connectionInternet(){
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        connectivityReceiver = ConnectivityReceiver(this) // Pasamos el this

        // Registrar el BroadcastReceiver para onservar cambios en la conectividad
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(connectivityReceiver, intentFilter)

    }

    fun bind() {
        toolbar = findViewById(R.id.myToolbar)
        drawerLayout = findViewById(R.id.drawer)
        navController = findNavController(R.id.fragmentContainerView)
        navigationVIew = findViewById(R.id.navigationView)
        noWifi = findViewById(R.id.NotWifi)


        val headerView = navigationVIew.getHeaderView(0)
        settingBottom = headerView.findViewById(R.id.setting_options)
        loginTextView = headerView.findViewById(R.id.textHeader)
        userName = headerView.findViewById(R.id.textUserName)
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }

    inner class ConnectivityReceiver(private val context: Context) : BroadcastReceiver() {

        private var isConnected = false
        private var isAnimationRunning = false
        private lateinit var fadeInAnimation: Animation
        override fun onReceive(context: Context?, intent: Intent?) {
            val networkInfo = connectivityManager.activeNetworkInfo
            isConnected = networkInfo != null && networkInfo.isConnected

            if (isConnected) {
                Toast.makeText(context, "Conectado", Toast.LENGTH_LONG).show()
                noWifi.visibility = ImageView.GONE
                stopAnimation()
            } else {
                Toast.makeText(context, "SIN CONECCION A INTERNET", Toast.LENGTH_LONG).show()
                noWifi.visibility = ImageView.VISIBLE
                startAnimation()
            }
        }

        private fun startAnimation() {
            if (!isAnimationRunning) {
                fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_animation)
                fadeInAnimation.setAnimationListener(object : Animation.AnimationListener {
                    override fun onAnimationStart(animation: Animation?) {
                        isAnimationRunning = true
                    }

                    override fun onAnimationEnd(animation: Animation?) {
                        if (!isConnected) {
                            // Si no hay conexión, repetir la animación
                            noWifi.startAnimation(fadeInAnimation)
                        } else {
                            stopAnimation()
                        }
                    }

                    override fun onAnimationRepeat(animation: Animation?) {
                        // No se utilizo
                    }
                })
                noWifi.startAnimation(fadeInAnimation)
            }
        }

        private fun stopAnimation() {
            if (isAnimationRunning) {
                noWifi.clearAnimation()
                isAnimationRunning = false
            }
        }
    }




    private fun navSetting() {

        settingBottom.setOnClickListener {
            navController.navigate(R.id.action_map_fragment_to_settingsFragment)
            drawerLayout.closeDrawer(GravityCompat.START)
        }

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


    //new
    private fun observeLoginData() {
        app.loginData.observe(this) { loginData ->
            if (loginData != null) {
                // Usuario logueado
                loginTextView.text = "Cerrar sesión"
                userName.text = loginData.user.name
                loginTextView.setOnClickListener {
                    app.clearLoginData()
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            } else {
                // Usuario no logueado
                loginTextView.text = "Iniciar sesión"
                userName.text = "Hello my brother"
                loginTextView.setOnClickListener {
                    navController.navigate(R.id.action_map_fragment_to_loginFragment)
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }
        }
    }

}

