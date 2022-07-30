package geekbrains.android.myweatherkotlin.view.view.weatherlist

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import geekbrains.android.myweatherkotlin.R
import geekbrains.android.myweatherkotlin.databinding.FragmentCitiesListBinding
import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.utils.SHARED_PREFERENCE_KEY_IS_RU
import geekbrains.android.myweatherkotlin.utils.SHARED_PREFERENCE_NAME_IS_RU
import geekbrains.android.myweatherkotlin.view.view.details.DetailsFragment
import geekbrains.android.myweatherkotlin.view.view.details.OnItemClick
import geekbrains.android.myweatherkotlin.viewmodel.citieslist.CitiesListViewModel
import geekbrains.android.myweatherkotlin.viewmodel.citieslist.CityListFragmentAppState

class CitiesListFragment : Fragment(), OnItemClick {

    companion object {
        fun newInstance() = CitiesListFragment()
    }

    var isRussian = true

    private var _binding: FragmentCitiesListBinding? = null
    private val binding: FragmentCitiesListBinding
        get() {
            return _binding!!
        }
    lateinit var viewModel: CitiesListViewModel

    private val REQUEST_CODE_LOCATION = 988

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCitiesListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shPref = requireActivity().getSharedPreferences(
            SHARED_PREFERENCE_NAME_IS_RU,
            Context.MODE_PRIVATE
        )
        isRussian = shPref.getBoolean(SHARED_PREFERENCE_KEY_IS_RU, true)

        viewModel = ViewModelProvider(this).get(CitiesListViewModel::class.java)
        viewModel.getLiveData()
            .observe(viewLifecycleOwner, object : Observer<CityListFragmentAppState> {
                override fun onChanged(t: CityListFragmentAppState) {
                    renderData(t)
                }
            })

        binding.weatherListFragmentFABCities.setOnClickListener {
            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherListForRussia()
                binding.weatherListFragmentFABCities.setImageResource(R.drawable.ic_europe)
            } else {
                viewModel.getWeatherListForEurope()
                binding.weatherListFragmentFABCities.setImageResource(R.drawable.ic_russia)
            }

            shPref.edit().apply() {
                putBoolean(SHARED_PREFERENCE_KEY_IS_RU, isRussian)
                apply()
            }
        }
        if (isRussian) {
            viewModel.getWeatherListForRussia()
        } else {
            viewModel.getWeatherListForEurope()
        }

        binding.weatherListFragmentFABLocation.setOnClickListener {
            checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    0L,
                    100F,
                    object : LocationListener {
                        override fun onLocationChanged(location: Location) {
                            Log.d("My_Log", "Координаты ${location.latitude} ${location.longitude}")
                        }
                    })
            }
        }
    }

    private fun permissionRequest(permission: String) {
        requestPermissions(arrayOf(permission), REQUEST_CODE_LOCATION)
    }

    private fun checkPermission(permission: String) {
        val permResult =
            ContextCompat.checkSelfPermission(requireContext(), permission)
        PackageManager.PERMISSION_GRANTED
        if (permResult == PackageManager.PERMISSION_GRANTED) {
            getLocation()
        } else if (shouldShowRequestPermissionRationale(permission)) {
            AlertDialog.Builder(requireContext())
                .setTitle(getString(R.string.dialog_rationale_title))
                .setMessage(getString(R.string.need_permission_text))
                .setPositiveButton(getString(R.string.need_permission_give)) { _, _ ->
                    permissionRequest(permission)
                }
                .setNegativeButton(getString(R.string.need_permission_dont_give)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        } else {
            permissionRequest(permission)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_LOCATION) {
            for (pIndex in permissions.indices) {
                if (permissions[pIndex] == Manifest.permission.ACCESS_FINE_LOCATION
                    && grantResults[pIndex] == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                    Log.d("My_Log", "OK!")
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun renderData(cityListFragmentAppState: CityListFragmentAppState) {
        when (cityListFragmentAppState) {

            is CityListFragmentAppState.Error -> {
                Log.d("My_Log", "Error")
                binding.root.homeWorkSnackbar(
                    "Ошибка",
                    Snackbar.LENGTH_LONG,
                    "Очередная попытка"
                ) { v: View ->
                    if (isRussian) {
                        viewModel.getWeatherListForRussia()
                        binding.weatherListFragmentFABCities.setImageResource(R.drawable.ic_europe)
                    } else {
                        viewModel.getWeatherListForEurope()
                        binding.weatherListFragmentFABCities.setImageResource(R.drawable.ic_russia)
                    }
                }
            }
            CityListFragmentAppState.Loading -> {
                Log.d("My_Log", "Loading")

            }
            is CityListFragmentAppState.SuccessSingle -> {
                val result = cityListFragmentAppState.weatherData
                Log.d("My_Log", "Success")
            }
            is CityListFragmentAppState.SuccessMulti -> {
                binding.mainFragmentRecyclerView.adapter =
                    DetailsListAdapter(cityListFragmentAppState.weatherList, this)
            }
        }
    }

    fun View.homeWorkSnackbar(
        string: String,
        duration: Int,
        actionString: String,
        block: (v: View) -> Unit
    ) {
        Snackbar.make(this, string, duration).setAction(actionString, block).show()
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .hide(this)
            .add(R.id.container, DetailsFragment.newInstance(weather))
            .addToBackStack("")
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}