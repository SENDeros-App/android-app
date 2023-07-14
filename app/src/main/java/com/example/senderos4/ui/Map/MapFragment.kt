package com.example.senderos4.ui.Map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.senderos4.R
import com.example.senderos4.SenderosApplication
import com.example.senderos4.data.Markers
import com.example.senderos4.databinding.FragmentMapBinding
import com.example.senderos4.databinding.FragmentRegisterBinding
import com.example.senderos4.network.socket.socketAlerts
import com.example.senderos4.ui.Map.MarkerType.*
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private lateinit var binding: FragmentMapBinding
    private lateinit var map: GoogleMap

    val app by lazy {
        requireActivity().application as SenderosApplication
    }

    private var loggedIn: Boolean = false
    private lateinit var user: String
    private lateinit var token: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getMapFragment()

        app.loginData.observe(requireActivity()) { loginData ->
            loggedIn = loginData != null

            user = loginData?.user?.name.toString()
            token = loginData?.token.toString()
        }

        socketAlerts.initSocket()
        socketAlerts.connect()

        socketAlerts.addMarkerListener(onNewMarker)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        enableLocation()
        map.setOnMyLocationClickListener(this)
        getCurrentLocation {
            moveCamera(it)
        }
        setAddAlertListener()
        map.setPadding(0, 1200, 20, 0)
        map.setOnMarkerClickListener(markerClickListener)
    }


    private val onNewMarker = Emitter.Listener { args ->
        requireActivity().runOnUiThread {
            val marcadoresJSON = args[0]
            when (marcadoresJSON) {
                is JSONObject -> {
                    // Si es uno
                    val markerJSON = marcadoresJSON
                    processMarker(markerJSON)
                }
                is JSONArray -> {
                    // Si son múltiples
                    for (i in 0 until marcadoresJSON.length()) {
                        val markerJSON = marcadoresJSON.getJSONObject(i)
                        processMarker(markerJSON)
                    }
                }
                else -> {
                    // Manejar otro caso
                    Log.e("tag", "Formato de marcadores no válido: $marcadoresJSON")
                }
            }
        }
    }

    private fun processMarker(markerJSON: JSONObject) {

        val user = markerJSON.getString("user")
        val latitude = markerJSON.getDouble("latitud")
        val longitude = markerJSON.getDouble("longitud")
        val name = markerJSON.getString("name")
        val type = markerJSON.getString("type")


        addMarkerAPI(user, name, longitude, latitude)
    }


    private fun addMarkerAPI(User:String, type:String, longitude:Double, latitude:Double) {

        val local = LatLng(latitude, longitude)

        var title = ""
        val description = "Author: $User"
        var icon = 0

        when (type) {
            "LIGHT" -> {
                title = "Sin Luz"
                icon = R.drawable.incident_without_ligth
            }

            "WATER" -> {
                title = "Sin Agua"
                icon = R.drawable.incident_water
            }

            "WALKAWAY" -> {
                title = "Psarela Dañada"
                icon = R.drawable.incident_walkaway
            }

            "LEAK_WATER" -> {
                title = "Fuja de agua"
                icon = R.drawable.incident_leak
            }

            "TREE" -> {
                title = "Árbol caído"
                icon = R.drawable.incident_tree
            }

            "SEWER" -> {
                title = "Alcantarilla sin tapa"
                icon = R.drawable.incident_sewer
            }

            "FIRE" -> {
                title = "Incendio"
                icon = R.drawable.incident_fire
            }

            "POTHOLE" -> {
                title = "Bache Peligroso"
                icon = R.drawable.incident_bump
            }

            "CRASH_CAR" -> {
                title = "Accidente automovilístico"
                icon = R.drawable.incident_crash
            }
        }
        map.addMarker(
            MarkerOptions().position(local).title(title).snippet(description).anchor(0.0f, 1.0f)
                .icon(bitMapFromVector(icon)))

    }



    private fun addedMarker(type: MarkerType) {

        getCurrentLocation { location ->
            val latitude = location.latitude
            val longitude = location.longitude
            val local = LatLng(latitude, longitude)

            var title = ""
            var id = ""
            val description = "Author: $user"
            var icon = 0

            when (type) {
                LIGHT -> {
                    title = "Sin Luz"
                    icon = R.drawable.incident_without_ligth
                    id = "64af2e47ba77f0b9c44e53de"
                }

                WATER -> {
                    title = "Sin Agua"
                    icon = R.drawable.incident_water
                    id = "64af2f0dba77f0b9c44e53e3"
                }

                WALKAWAY -> {
                    title = "Pasarela Dañada"
                    icon = R.drawable.incident_walkaway
                    id = "64af2f6cba77f0b9c44e53ef"
                }

                LEAK_WATER -> {
                    title = "Fuja de agua"
                    icon = R.drawable.incident_leak
                    id = "64af2f3bba77f0b9c44e53e9"
                }

                TREE -> {
                    title = "Árbol caído"
                    icon = R.drawable.incident_tree
                    id = "64af2f8dba77f0b9c44e53f2"
                }

                SEWER -> {
                    title = "Alcantarilla sin tapa"
                    icon = R.drawable.incident_sewer
                    id = "64af2f59ba77f0b9c44e53ec"
                }

                FIRE -> {
                    title = "Incendio"
                    icon = R.drawable.incident_fire
                    id = "64af2f2bba77f0b9c44e53e6"
                }

                POTHOLE -> {
                    title = "Bache Peligroso"
                    icon = R.drawable.incident_bump
                    id = "64af2f9bba77f0b9c44e53f5"
                }

                CRASH_CAR -> {
                    title = "Accidente automovilístico"
                    icon = R.drawable.incident_crash
                    id = "64af2fa9ba77f0b9c44e53f8"
                }
            }
            map.addMarker(
                MarkerOptions().position(local).title(title).snippet(description).anchor(0.0f, 1.0f)
                    .icon(bitMapFromVector(icon))
            )


            val marcador =
                Markers(token, user, latitude.toString(), longitude.toString(), type.toString(), id)

            socketAlerts.emitCreatedMarker(marcador)
        }


    }

    @SuppressLint("InflateParams")
    private fun setAddAlertListener() {
        binding.btnAlert.setOnClickListener {
            createDialog()
        }

    }

    private fun dialogWarning() {
        val dialog_warning = Dialog(requireContext())
        dialog_warning.setCancelable(true)
        dialog_warning.setContentView(R.layout.dialog_warning)
        dialog_warning.show()


        val btn_login: Button = dialog_warning.findViewById(R.id.login_btn)
        val btn_register: Button = dialog_warning.findViewById(R.id.register_btn)

        btn_login.setOnClickListener {
            findNavController().navigate(R.id.action_map_fragment_to_loginFragment)
            dialog_warning.dismiss()
        }

        btn_register.setOnClickListener {
            findNavController().navigate(R.id.action_map_fragment_to_registerFragment)
            dialog_warning.dismiss()
        }

    }

    private fun createDialog() {
        // Función que cree el dialogo
        val dialog_alerts = Dialog(requireContext())
        dialog_alerts.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog_alerts.setCancelable(true)
        dialog_alerts.setContentView(R.layout.dialog_alerts)
        dialog_alerts.setTitle("Actualiza el mapa")


        val without_light: ImageView = dialog_alerts.findViewById(R.id.without_light)
        val leak: ImageView = dialog_alerts.findViewById(R.id.leak)
        val water: ImageView = dialog_alerts.findViewById(R.id.water)
        val fire: ImageView = dialog_alerts.findViewById(R.id.fire)
        val sewer: ImageView = dialog_alerts.findViewById(R.id.sewer)
        val walkaway: ImageView = dialog_alerts.findViewById(R.id.walkaway)
        val pothole: ImageView = dialog_alerts.findViewById(R.id.pothole)
        val tree: ImageView = dialog_alerts.findViewById(R.id.tree)
        val crash: ImageView = dialog_alerts.findViewById(R.id.crash)

        dialog_alerts.show()

        leak.setOnClickListener {
            handleClick(R.id.leak)
            dialog_alerts.dismiss()
        }

        without_light.setOnClickListener {
            handleClick(R.id.without_light)
            dialog_alerts.dismiss()
        }

        water.setOnClickListener {
            handleClick(R.id.water)
            dialog_alerts.dismiss()
        }

        fire.setOnClickListener {
            handleClick(R.id.fire)
            dialog_alerts.dismiss()
        }

        sewer.setOnClickListener {
            handleClick(R.id.sewer)
            dialog_alerts.dismiss()
        }

        walkaway.setOnClickListener {
            handleClick(R.id.walkaway)
            dialog_alerts.dismiss()
        }

        pothole.setOnClickListener {
            handleClick(R.id.pothole)
            dialog_alerts.dismiss()
        }

        tree.setOnClickListener {
            handleClick(R.id.tree)
            dialog_alerts.dismiss()
        }

        crash.setOnClickListener {
            handleClick(R.id.crash)
            dialog_alerts.dismiss()
        }
    }

    private fun handleClick(id: Int) {
        if (loggedIn) {
            when (id) {
                R.id.leak -> addedImageDialog(LEAK_WATER)
                R.id.without_light -> addedImageDialog(LIGHT)
                R.id.water -> addedImageDialog(WATER)
                R.id.fire -> addedImageDialog(WATER)
                R.id.sewer -> addedImageDialog(WATER)
                R.id.walkaway -> addedImageDialog(WALKAWAY)
                R.id.pothole -> addedImageDialog(POTHOLE)
                R.id.tree -> addedImageDialog(TREE)
                R.id.crash -> addedImageDialog(TREE)
            }
        } else {
            dialogWarning()
        }
    }

    private fun addedImageDialog(type: MarkerType) {
        var image = 0
        var text = ""

        when (type) {
            LIGHT -> {
                image = R.drawable.without_ligth
                text = "Sin Luz"

            }

            WATER -> {
                image = R.drawable.without_water
                text = "Sin agua"
            }

            WALKAWAY -> {
                image = R.drawable.walkaway
                text = "Pasarela Dañada"
            }

            LEAK_WATER -> {
                image = R.drawable.leak
                text = "Fuga de agua"
            }

            TREE -> {
                image = R.drawable.tree
                text = "Árbol caído"
            }

            SEWER -> {
                image = R.drawable.sewer
                text = "Alcantarilla sin tapa"
            }

            FIRE -> {
                image = R.drawable.fire_
                text = "Incendio"
            }

            POTHOLE -> {
                image = R.drawable.pothole
                text = "Bache peligroso"
            }

            CRASH_CAR -> {
                image = R.drawable.crash_car
                text = "Accidente automivilistico"
            }

        }
        newDialog(image, text, type)
    }


    private fun newDialog(image: Int, text: String, type: MarkerType) {
        val dialog_alert = Dialog(requireContext())
        dialog_alert.setCancelable(true)
        dialog_alert.setContentView(R.layout.dialog_by_alert)
        dialog_alert.setTitle("Titulo")
        val imageView: ImageView = dialog_alert.findViewById(R.id.icon_dialog_alert)
        val textView: TextView = dialog_alert.findViewById(R.id.name_alert)
        val btn: Button = dialog_alert.findViewById(R.id.send_alert)
        textView.text = text
        imageView.setImageResource(image)
        dialog_alert.show()

        btn.setOnClickListener {
            addedMarker(type)
            dialog_alert.dismiss()
        }
    }


    val markerClickListener = GoogleMap.OnMarkerClickListener { marker ->
        false
    }


    private fun bitMapFromVector(vectorResID: Int): BitmapDescriptor {
        val vectorDrawable = ContextCompat.getDrawable(requireContext(), vectorResID)
        vectorDrawable!!.setBounds(
            0, 0, vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight
        )
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth, vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    private fun getMapFragment() {
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(onComplete: (Location) -> Unit) {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        val locationTask: Task<Location> =
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        locationTask.addOnSuccessListener { currentLocation ->
            onComplete(currentLocation)
            socketAlerts.userUbication(currentLocation.latitude, currentLocation.longitude)
        }
    }

    private fun moveCamera(location: Location) {
        val ubi = LatLng(location.latitude, location.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLng(ubi))
        val cameraPosition =
            CameraPosition.builder().target(ubi).zoom(18f).bearing(0f).tilt(45f).build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun isLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.isMyLocationEnabled = true
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                requireContext(), "Va a ajustes y acepta los permisos", Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return
                }
                map.isMyLocationEnabled = true
            } else {
                Toast.makeText(
                    requireContext(),
                    "Para activar la localización ve a los ajuste y activa los permisos",
                    Toast.LENGTH_SHORT
                ).show()
            }

            else -> {}
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if (!::map.isInitialized) return
        if (!isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            map.isMyLocationEnabled = false
            Toast.makeText(
                requireContext(),
                "Para activar la localización ve a los ajuste y activa los permisos",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(requireContext(), "Boton pulsado", Toast.LENGTH_LONG).show()
        return true
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(
            requireContext(), "Estás en ${p0.latitude}, ${p0.longitude}", Toast.LENGTH_SHORT
        ).show()
    }

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }

}