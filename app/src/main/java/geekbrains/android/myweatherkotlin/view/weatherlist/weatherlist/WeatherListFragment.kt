package geekbrains.android.myweatherkotlin.view.weatherlist.weatherlist

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
import geekbrains.android.myweatherkotlin.databinding.FragmentWeatherListBinding
import geekbrains.android.myweatherkotlin.view.weatherlist.details.DetailsFragment
import geekbrains.android.myweatherkotlin.view.weatherlist.details.OnItemClick
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

            is AppState.Error -> {
                Log.d("My_Log", "Error")
                binding.root.homeWorkSnackbar("Ошибка", Snackbar.LENGTH_LONG, "Очередная попытка") { v: View ->
                    if (isRussian) {
                        viewModel.getWeatherListForRussia()
                    } else {
                        viewModel.getWeatherListForEurope()
                    }
                }
            }
            AppState.Loading -> {
                Log.d("My_Log", "Loading")

                //работа функции высшего порядка
                Log.d("My_Log", mathSum(10) {it * it}.toString())
                Log.d("My_Log", mathSum(10, ::fibonacci).toString())
            }
            is AppState.SuccessSingle -> {
                val result = appState.weatherData
                Log.d("My_Log", "Success")
            }
            is AppState.SuccessMulti -> {
                binding.mainFragmentRecyclerView.adapter = WeatherListAdapter(appState.weatherList, this)
            }
        }
    }

    fun View.homeWorkSnackbar(string: String, duration: Int, actionString: String, block: (v: View) -> Unit) {
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

    fun mathSum(length: Int, series: (Int) -> Int): Int {
        var result = 0
        for (i in 0..length) {
            result += series(i)
        }
        return result
    }

    fun fibonacci(number: Int): Int {
        if (number <= 0) {
            return 0
        }
        if (number == 1 || number == 2) {
            return 1
        }
        return fibonacci(number - 1) + fibonacci(number - 2)
    }

}