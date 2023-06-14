package com.example.senderos4.ui.Map

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
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


class MapFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    private lateinit var btn_select_alert: ImageView
    private lateinit var map: GoogleMap

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getMapFragment()
        bind()
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
    }

    private fun bind() {
        btn_select_alert = requireView().findViewById(R.id.btn_alert)
    }

    @SuppressLint("InflateParams")
    private fun setAddAlertListener() {
        btn_select_alert.setOnClickListener {
            // Función que cree el dialogo
            val dialog = Dialog(requireContext())
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.setContentView(R.layout.dialog_alerts)
            dialog.setTitle("Actualiza el mapa")

            val without_light: ImageView = dialog.findViewById(R.id.without_light)
            val leak: ImageView = dialog.findViewById(R.id.leak)
            val water: ImageView = dialog.findViewById(R.id.water)
            val fire: ImageView = dialog.findViewById(R.id.fire)
            val sewer: ImageView = dialog.findViewById(R.id.sewer)
            val walkaway: ImageView = dialog.findViewById(R.id.walkaway)
            val pothole: ImageView = dialog.findViewById(R.id.pothole)
            val tree: ImageView = dialog.findViewById(R.id.tree)
            val crash: ImageView = dialog.findViewById(R.id.crash)

            dialog.show()

            leak.setOnClickListener {
                addedImageDialog(MarkerType.LEAK_WATER)
            }

            without_light.setOnClickListener {
                addedImageDialog(MarkerType.LIGHT)
            }

            water.setOnClickListener {
                addedImageDialog(MarkerType.WATER)
            }

            fire.setOnClickListener {
                addedImageDialog(MarkerType.FIRE)
            }

            sewer.setOnClickListener {
                addedImageDialog(MarkerType.SEWER)
            }

            walkaway.setOnClickListener {
                addedImageDialog(MarkerType.WALKAWAY)
            }

            pothole.setOnClickListener {
                addedImageDialog(MarkerType.POTHOLE)
            }

            tree.setOnClickListener {
                addedImageDialog(MarkerType.TREE)
            }

            crash.setOnClickListener {
                addedImageDialog(MarkerType.CRASH_CAR)
            }
        }

    }

    private fun addedImageDialog(type: MarkerType) {
        var image = 0


        when (type) {
            MarkerType.LIGHT -> {
                image = R.drawable.without_ligth
            }

            MarkerType.WATER -> {
                image = R.drawable.without_water
            }

            MarkerType.WALKAWAY -> {
                image = R.drawable.walkaway
            }

            MarkerType.LEAK_WATER -> {
                image = R.drawable.leak
            }

            MarkerType.TREE -> {
                image = R.drawable.tree
            }

            MarkerType.SEWER -> {
                image = R.drawable.sewer
            }

            MarkerType.FIRE -> {
                image = R.drawable.fire_
            }

            MarkerType.POTHOLE -> {
                image = R.drawable.pothole
            }

            MarkerType.CRASH_CAR -> {
                image = R.drawable.crash_car
            }
        }

        val dialog = Dialog(requireContext())
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.dialog_by_alert)
        dialog.setTitle("Titulo")
        val imageView: ImageView = dialog.findViewById(R.id.icon_dialog_alert)
        imageView.setImageResource(image)
        dialog.show()
    }

    private fun addedMarker(type: MarkerType) {

        getCurrentLocation { location ->
            val latitude = location.latitude
            val longitude = location.longitude
            val local = LatLng(latitude, longitude)

            var title = ""
            var description = ""
            var icon = 0

            when (type) {
                MarkerType.LIGHT -> {
                    title = "Sin Luz"
                    description = "No hay luz"
                    icon = R.drawable.incident_without_ligth
                }

                MarkerType.WATER -> {
                    title = "Sin Agua"
                    description = "No hay agua"
                    icon = R.drawable.incident_water
                }

                MarkerType.WALKAWAY -> {
                    title = "Psarela Dañada"
                    description = "Pasarela tiene daños precaución"
                    icon = R.drawable.incident_walkaway
                }

                MarkerType.LEAK_WATER -> {
                    title = "Fuja de agua"
                    description = "Aqui hay una fuja de agua"
                    icon = R.drawable.incident_leak
                }

                MarkerType.TREE -> {
                    title = "Árbol caído"
                    description = "El árbol se encuentra obstaculizando el paso"
                    icon = R.drawable.incident_tree
                }

                MarkerType.SEWER -> {
                    title = "Alcantarilla sin tapa"
                    description = "Cuidado la alcantarilla se encuentra sin tapa puedes caer"
                    icon = R.drawable.incident_sewer
                }

                MarkerType.FIRE -> {
                    title = "Incendio"
                    description = "Aqui hay un incendio"
                    icon = R.drawable.incident_fire
                }

                MarkerType.POTHOLE -> {
                    title = "Bache Peligroso"
                    description = "Hay un bache muy peligroso que puede ocasionar un accidente"
                    icon = R.drawable.incident_bump
                }

                MarkerType.CRASH_CAR -> {
                    title = "Accidente automovilístico"
                    description = "Hay una accidente de carros por la zona"
                    icon = R.drawable.incident_crash
                }
            }
            map.addMarker(
                MarkerOptions().position(local).title(title).snippet(description).anchor(0.0f, 1.0f)
                    .icon(bitMapFromVector(icon))
            )
        }

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