package com.example.senderos4.fragment.Map

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.senderos4.R
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
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task


class MapFragment : Fragment(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

    private lateinit var buttonAlert:Button

    private lateinit var map: GoogleMap

    companion object {
        const val REQUEST_CODE_LOCATION = 0
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createFragment()
        bind()
    }

    private fun bind(){
        buttonAlert = requireView().findViewById(R.id.buttonAlert)
    }

    @SuppressLint("InflateParams")
    private fun addListener(/*location: Location*/) {

        buttonAlert.setOnClickListener{

            addedMarker()

            /*val builder = AlertDialog.Builder(requireContext())
            val view = layoutInflater.inflate(R.layout.dialog_alerts, null)

            builder.setView(view)

            val dialog = builder.create()
            dialog.show()*/

        }

    }

    fun addedMarker(/*location: Location*/) {

        getCurrentLocation { location ->
            val latitude = location.latitude
            val longitude = location.longitude

            val local = LatLng(latitude, longitude)

            fun bitMapFromVector(vectorResID:Int): BitmapDescriptor {
                val vectorDrawable=ContextCompat.getDrawable(requireContext(),vectorResID)
                vectorDrawable!!.setBounds(0,0,vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight)
                val bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                vectorDrawable.draw(canvas)
                return BitmapDescriptorFactory.fromBitmap(bitmap)
            }

            println(currentLocation)


            map.addMarker(
                MarkerOptions()
                    .position(local)
                    .title("Prueba")
                    .snippet("Descripción del marcador")
                    .anchor(0.0f, 1.5f)
                    .icon(bitMapFromVector(R.drawable.incident_water))
            )
        }

    }

    private fun createFragment() {
        val mapFragment: SupportMapFragment =
            childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /*val mapFragment = MapFragment.newInstance(
        GoogleMapOptions()
            .mapId(resources.getString(R.string.map_id))
    )*/

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        enableLocation()
        map.setOnMyLocationClickListener(this)

        getCurrentLocation {
            moveCamera(it)
        }


        addListener()
        map.setPadding(0, 1200, 0, 0)
    }


    private var currentLocation: Location? = null

    @SuppressWarnings
     private fun getCurrentLocation(onComplete: (Location) -> Unit) {
        val fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        val locationTask: Task<Location> =
            fusedLocationProviderClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        locationTask.addOnSuccessListener { location ->
            currentLocation = location
            onComplete(location)
        }
    }
    private fun moveCamera(location: Location) {
        val ubi = LatLng(location.latitude, location.longitude)
        map.moveCamera(CameraUpdateFactory.newLatLng(ubi))
        val cameraPosition = CameraPosition.builder()
            .target(ubi)
            .zoom(18f)
            .bearing(0f)
            .tilt(45f)
            .build()
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun isLocationPermissionGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun enableLocation() {
        if (!::map.isInitialized) return
        if (isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
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
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(
                requireContext(),
                "Va a ajustes y acepta los permisos",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
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

    override fun onResume() {
        super.onResume()
        if (!::map.isInitialized) return
        if (!isLocationPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
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
            requireContext(),
            "Estás en ${p0.latitude}, ${p0.longitude}",
            Toast.LENGTH_SHORT
        ).show()
    }

}