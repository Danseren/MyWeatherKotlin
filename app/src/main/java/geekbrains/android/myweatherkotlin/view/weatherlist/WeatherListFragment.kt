package geekbrains.android.myweatherkotlin.view.weatherlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import geekbrains.android.myweatherkotlin.databinding.FragmentWeatherListBinding
import geekbrains.android.myweatherkotlin.viewmodel.AppState
import geekbrains.android.myweatherkotlin.viewmodel.WeatherListViewModel

class WeatherListFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherListFragment()
    }

    lateinit var binding: FragmentWeatherListBinding
    lateinit var viewModel: WeatherListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeatherListBinding.inflate(inflater)
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
        viewModel.sentRequest()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            //Не разобрался до конца, что надо здесь выводить при Error и при Loading
            //Особенно не понятно почему при Error в логи так ничего и не выводится
            is AppState.Error -> Log.d("My_Log", "Error")
            AppState.Loading -> Log.d("My_Log", "Loading")
            is AppState.Success -> {
                val result = appState.weatherData
                binding.tvCityName.text = result.city.name
                binding.tvTemperatureValue.text = result.temperature.toString()
                binding.tvFeelsLikeValue.text = result.feelsLike.toString()
                binding.tvCityCoordinates.text = "Широта:  ${result.city.latitude}\nДолгота: ${result.city.longitude}"
                Log.d("My_Log", "Success")
            }
        }
    }
}