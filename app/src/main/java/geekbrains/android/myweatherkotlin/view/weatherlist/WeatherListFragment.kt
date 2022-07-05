package geekbrains.android.myweatherkotlin.view.weatherlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import geekbrains.android.myweatherkotlin.R
import geekbrains.android.myweatherkotlin.databinding.FragmentWeatherListBinding
import geekbrains.android.myweatherkotlin.details.DetailsFragment
import geekbrains.android.myweatherkotlin.details.OnItemClick
import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.viewmodel.AppState
import geekbrains.android.myweatherkotlin.viewmodel.WeatherListViewModel

class WeatherListFragment : Fragment(), OnItemClick {

    companion object {
        fun newInstance() = WeatherListFragment()
    }

    var isRussian = true

    private var _binding: FragmentWeatherListBinding? = null
    private val binding: FragmentWeatherListBinding
    get() {
        return _binding!!
    }
    lateinit var viewModel: WeatherListViewModel

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherListViewModel::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, object : Observer<AppState>{
            override fun onChanged(t: AppState) {
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
        }
        viewModel.getWeatherListForRussia()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            //Не разобрался до конца, что надо здесь выводить при Error и при Loading
            //Особенно не понятно почему при Error в логи так ничего и не выводится
            is AppState.Error -> Log.d("My_Log", "Error")
            AppState.Loading -> Log.d("My_Log", "Loading")
            is AppState.SuccessSingle -> {
                val result = appState.weatherData
                Log.d("My_Log", "Success")
            }
            is AppState.SuccessMulti -> {
                binding.mainFragmentRecyclerView.adapter = WeatherListAdapter(appState.weatherList, this)
            }
        }
    }

    override fun onItemClick(weather: Weather) {
        requireActivity().supportFragmentManager
            .beginTransaction()
            .hide(this)
            .add(R.id.container, DetailsFragment.newInstance(weather))
            .addToBackStack("")
            .commit()
    }
}