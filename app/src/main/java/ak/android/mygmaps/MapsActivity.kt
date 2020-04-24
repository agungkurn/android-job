package ak.android.mygmaps

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_maps.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val RC_PERMISSION_LOCATION = 100
    private val TAG = MapsActivity::class.java.simpleName

    private val markers = mutableListOf<Marker>()

    private val mapFragment by lazy {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
    }

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MapsViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
        ) {
            mapFragment.getMapAsync(this)
        } else {
            askPermission()
        }
    }

    override fun onResume() {
        super.onResume()

        val behavior = BottomSheetBehavior.from(rv_blue_dot)
        behavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onMapReady(googleMap: GoogleMap) {
        fusedLocationProviderClient.lastLocation
			.addOnSuccessListener {
            	Log.d(
                	TAG,
                	"Lat: ${it.latitude}. Long: ${it.longitude}"
            	)

            	// Move camera on user's last known location
            	googleMap.moveCamera(
                	CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 14f)
            	)

            	// Clear anything when the camera is being moved
            	googleMap.setOnCameraMoveListener {
                	googleMap.clear()
            	}

            	// Add marker at the center of current camera position
            	// and generate 10 blue dots around it
            	googleMap.setOnCameraIdleListener {
                	val center = googleMap.cameraPosition.target
                	showCurrentLocation(center, googleMap)
                	showBlueDots(center, googleMap)
            }
        	}.addOnFailureListener {
            	Toast.makeText(applicationContext, "Error: ${it.localizedMessage}", Toast.LENGTH_SHORT)
                	.show()
        	}
    }

    private fun showCurrentLocation(latLong: LatLng, map: GoogleMap) {
        // main marker
        map.addMarker(
            MarkerOptions()
                .position(latLong)
                .title("user")
        )

        // parameter circle
        map.addCircle(
            CircleOptions()
                .center(latLong)
                .radius(1000.0)
        )
    }

    private fun showBlueDots(latLng: LatLng, googleMap: GoogleMap) {
        viewModel.blueDots(latLng).observe(this, Observer { dots ->
            Log.d(TAG, "observed")
            dots.forEach { position ->
                Log.d(TAG, "Marker position: $position")
                googleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(position.latitude!!, position.longitude!!))
                        .icon(
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
                        )
                )
            }

            BlueDotAdapter(dots).also {
                rv_blue_dot.adapter = it
                it.notifyDataSetChanged()
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == RC_PERMISSION_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mapFragment.getMapAsync(this)
            }
        }
    }

    private fun askPermission() {
        // ask for permission
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), RC_PERMISSION_LOCATION
        )
    }
}
