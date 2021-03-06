package com.example.angkoot.ui.ordering

import android.Manifest
import android.content.IntentSender
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.angkoot.databinding.FragmentOrderingBinding
import com.example.angkoot.domain.model.Place
import com.example.angkoot.utils.PermissionUtils
import com.example.angkoot.utils.PermissionUtils.REQUEST_CODE_LOCATION_PERMISSION
import com.example.angkoot.utils.ToastUtils
import com.example.angkoot.utils.ext.hide
import com.example.angkoot.utils.ext.show
import com.example.angkoot.vo.StatusRes
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.IOException
import java.util.*

@FlowPreview
@AndroidEntryPoint
class OrderingFragment : Fragment(), OnMapReadyCallback, EasyPermissions.PermissionCallbacks,
    PlacesAdapter.OnClickCallback {
    private var _binding: FragmentOrderingBinding? = null
    private val binding get() = _binding!!
    private var _view: View? = null
    private var placesAdapter: PlacesAdapter? = null

    private val viewModel: OrderingViewModel by viewModels()

    private var _fusedLocationProviderClient: FusedLocationProviderClient? = null
    private val fusedLocationProviderClient: FusedLocationProviderClient get() = _fusedLocationProviderClient!!

    private var _googleMap: GoogleMap? = null
    private val googleMap: GoogleMap get() = _googleMap!!

    private var _geoCoder: Geocoder? = null
    private val geoCoder: Geocoder get() = _geoCoder!!

    private var currentLocationMarker: Marker? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOrderingBinding.inflate(inflater, container, false)
        _view = _binding?.root
        return _view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapViewOrdering.onCreate(savedInstanceState)

        BottomSheetBehavior.from(binding.sheet).apply {
            peekHeight = 100
            state = BottomSheetBehavior.STATE_EXPANDED
        }

        requestPermission()
        initUI()
        observeData()
    }

    private fun initUI() {
        _fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        _geoCoder = Geocoder(requireContext(), Locale.getDefault())

        placesAdapter = PlacesAdapter()
        placesAdapter?.setItemCallback(this)

        with(binding) {
            mapViewOrdering.getMapAsync(this@OrderingFragment)

            with(rvSearchingResults) {
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                setHasFixedSize(true)
                adapter = placesAdapter
            }

            svPickup.setOnQueryTextListener(svPickupListener)
            svDrop.setOnQueryTextListener(svDropListener)
        }
    }

    private fun observeData() {
        with(viewModel) {
            getSearchingPlacesPickupResults().observe(viewLifecycleOwner) {
                when (it.status) {
                    StatusRes.LOADING -> {
                        binding.progressbar.show()
                    }
                    StatusRes.ERROR -> {
                        binding.progressbar.hide()
                    }
                    StatusRes.SUCCESS -> {
                        Log.d("Hehe", "Data: ${it.data}")
                        Log.d("Hehe", "Message: ${it.message}")
                        binding.progressbar.hide()
                    }
                }
            }

            getSearchingPlacesDropResults().observe(viewLifecycleOwner) {
                when (it.status) {
                    StatusRes.LOADING -> {
                        binding.progressbar.show()
                        binding.rvSearchingResults.hide()
                    }
                    StatusRes.ERROR -> {
                        binding.progressbar.hide()
                        binding.rvSearchingResults.hide()
                    }
                    StatusRes.SUCCESS -> {
                        Log.d("Hehe", "Data: ${it.data}")
                        Log.d("Hehe", "Message: ${it.message}")
                        if (it.data != null && it.data.isNotEmpty()) {
                            placesAdapter?.submitList(it.data)
                        }
                        binding.rvSearchingResults.show()
                        binding.progressbar.hide()
                    }
                }
            }
        }
    }

    private fun setMarker(latLng: LatLng, locationName: String) {
        currentLocationMarker?.remove()

        with(googleMap) {
            currentLocationMarker = addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("Current Location")
                    .snippet(locationName)
                    .icon(
                        BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_GREEN
                        )
                    )
            )

            moveMapCameraTo(latLng)
        }
    }

    private fun moveMapCameraTo(latLng: LatLng, zoom: Float = DEFAULT_ZOOM) {
        with(googleMap) {
            val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(zoom)
                .build()

            moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        }
    }

    private fun getCurrentLocation() {
        val locationRequest = LocationRequest().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = (10 * 1000).toLong()
            fastestInterval = 2000
        }

        val locationSettingsRequest = LocationSettingsRequest.Builder().apply {
            addLocationRequest(locationRequest)
        }.build()

        LocationServices.getSettingsClient(requireContext())
            .checkLocationSettings(locationSettingsRequest).apply {
                addOnCompleteListener { task ->
                    try {
                        val response = task.getResult(ApiException::class.java)

                        if (response != null && response.locationSettingsStates.isLocationPresent) {
                            getLastLocation()
                        }
                    } catch (exception: ApiException) {
                        when (exception.statusCode) {
                            LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                                val resolvable = exception as ResolvableApiException
                                resolvable.startResolutionForResult(
                                    requireActivity(),
                                    REQUEST_CHECK_SETTING
                                )
                            } catch (e: IntentSender.SendIntentException) {
                            } catch (e: ClassCastException) {
                            }

                            LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            }
                        }
                    }
                }
            }
    }

    private fun getLastLocation() {
        if (PermissionUtils.checkLocationPermission(requireContext())) {
            fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    val lastLocation = it.result

                    try {
                        val addressList = geoCoder.getFromLocation(
                            lastLocation.latitude,
                            lastLocation.longitude,
                            1
                        )

                        val currentAddress = addressList[0]
                        //latitude longtitude
                        currentLocationMarker?.remove()
                        moveMapCameraTo(LatLng(currentAddress.latitude, currentAddress.longitude))
                        googleMap.isMyLocationEnabled = true

                        viewModel.setLatLngPickup(
                            LatLng(
                                lastLocation.latitude,
                                lastLocation.longitude,
                            )
                        )

                        with(binding) {
                            svPickup.queryHint = "Current Location set"
                            svDrop.isIconified = true
                            svDrop.requestFocus()
                        }

                        with(binding) {
                            mapViewOrdering.show()
                            progressbar.hide()
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                } else {
                    ToastUtils.show(
                        requireContext(),
                        "No current location found"
                    )

                    moveMapCameraTo(LatLng(-6.23139938, 106.95464244))
                    googleMap.isMyLocationEnabled = true
                }
            }
        }
    }

    override fun onClick(place: Place) {
        binding.rvSearchingResults.hide()
    }

    private fun requestPermission() {
        if (PermissionUtils.hasLocationPermission(requireContext())) return

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this application",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use this application",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    // PERMISSIONS
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    // LIFECYCLE
    override fun onMapReady(googleMap: GoogleMap?) {
        _googleMap = googleMap
        getCurrentLocation()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _fusedLocationProviderClient = null
        _googleMap = null
        currentLocationMarker = null
        placesAdapter?.setItemCallback(null)
        placesAdapter = null
        _binding = null
        _view = null
    }

    override fun onResume() {
        super.onResume()
        getLastLocation()
        _binding?.mapViewOrdering?.onResume()
    }

    override fun onStart() {
        super.onStart()
        _binding?.mapViewOrdering?.onStart()
    }

    override fun onPause() {
        super.onPause()
        _binding?.mapViewOrdering?.onPause()
    }

    override fun onStop() {
        super.onStop()
        _binding?.mapViewOrdering?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        _binding?.mapViewOrdering?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        _binding?.mapViewOrdering?.onSaveInstanceState(outState)
    }

    private val svPickupListener = object : android.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean =
            false

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null)
                viewModel.setQueryForSearchingPlacesPickup(newText)

            return true
        }
    }

    private val svDropListener = object : android.widget.SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean =
            false

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText != null)
                viewModel.setQueryForSearchingPlacesDrop(newText)

            return true
        }
    }

    // CONSTANTS
    companion object {
        const val DEFAULT_ZOOM = 17f
        const val REQUEST_CHECK_SETTING = 120
    }
}