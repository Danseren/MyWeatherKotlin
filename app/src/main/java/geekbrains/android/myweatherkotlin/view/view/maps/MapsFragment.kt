package geekbrains.android.myweatherkotlin.view.view.maps

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import geekbrains.android.myweatherkotlin.R
import geekbrains.android.myweatherkotlin.databinding.FragmentMapsUiBinding

class MapsFragment : Fragment() {

    lateinit var map: GoogleMap

    private val callback = OnMapReadyCallback { googleMap ->

        map = googleMap
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

        googleMap.setOnMapLongClickListener { latLng ->
            addMarkerToArray(latLng)
            setMarker(latLng, "", R.drawable.ic_map_marker)
            drawLine()
        }

        googleMap.uiSettings.isZoomControlsEnabled = true

        val permResult =
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        PackageManager.PERMISSION_GRANTED
        if (permResult == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.need_permission_text))
                .setPositiveButton(getString(R.string.need_permission_give)) { _, _ ->
                    permissionRequest(Manifest.permission.ACCESS_FINE_LOCATION)
                }
                .setNegativeButton(getString(R.string.need_permission_dont_give)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            permissionRequest(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private var _binding: FragmentMapsUiBinding? = null
    private val binding: FragmentMapsUiBinding
        get() {
            return _binding!!
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsUiBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.buttonSearch.setOnClickListener {
            binding.searchAddress.text.toString().let { searchText ->
                val geocoder = Geocoder(requireContext())
                val result = geocoder.getFromLocationName(searchText, 1)

                val ln = LatLng(result.first().latitude, result.first().longitude)
                setMarker(ln, searchText, R.drawable.ic_map_marker)
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(ln, 15f))
            }
        }
    }

    private val REQUEST_CODE_LOCATION = 101

    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_LOCATION)
    }

    val markers = mutableListOf<Marker>()

    private fun addMarkerToArray(location: LatLng) {
        val marker = setMarker(location, markers.size.toString(), R.drawable.ic_map_pin)
        markers.add(marker)
    }

    private fun drawLine() {
        val last: Int = markers.size - 1
        if (last >= 1) {
            val previous: LatLng = markers[last - 1].position
            val current: LatLng = markers[last].position
            map.addPolyline(
                PolylineOptions()
                    .add(previous, current)
                    .color(Color.RED)
                    .width(15f)
            )
        }
    }

    private fun setMarker(
        location: LatLng,
        searchText: String,
        resourcedId: Int
    ): Marker {
        return map.addMarker(
            MarkerOptions()
                .position(location)
                .title(searchText)
                .icon(BitmapDescriptorFactory.fromResource(resourcedId))
        )!!
    }
}