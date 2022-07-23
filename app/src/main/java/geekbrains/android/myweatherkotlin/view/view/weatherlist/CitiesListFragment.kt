package geekbrains.android.myweatherkotlin.view.view.weatherlist

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.weatherListFragmentFAB.setOnClickListener {
            isRussian = !isRussian
            if (isRussian) {
                viewModel.getWeatherListForRussia()
                binding.weatherListFragmentFAB.setImageResource(R.drawable.ic_europe)
            } else {
                viewModel.getWeatherListForEurope()
                binding.weatherListFragmentFAB.setImageResource(R.drawable.ic_russia)
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
                        binding.weatherListFragmentFAB.setImageResource(R.drawable.ic_europe)
                    } else {
                        viewModel.getWeatherListForEurope()
                        binding.weatherListFragmentFAB.setImageResource(R.drawable.ic_russia)
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