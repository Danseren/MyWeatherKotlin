package geekbrains.android.myweatherkotlin.view.weatherlist.details

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import geekbrains.android.myweatherkotlin.BuildConfig
import geekbrains.android.myweatherkotlin.databinding.FragmentDetailsBinding
import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import geekbrains.android.myweatherkotlin.utils.*
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }

    val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            Log.d("My_Log", "onReceive ${binding.root}")
            intent?.let {
                it.getParcelableExtra<WeatherDTO>(BUNDLE_WEATHER_DTO_KEY)?.let { weatherDTO ->
                    bindWeatherLocalWithWeatherDTO(weatherLocal, weatherDTO)
                }
            }
        }

    }

    lateinit var weatherLocal: Weather

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val weather = arguments?.let { arg ->
            arg.getParcelable<Weather>(BUNDLE_WEATHER_EXTRA)
        }
        weather?.let { weatherLocal ->
            this.weatherLocal = weatherLocal

//            LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
//                receiver, IntentFilter(
//                    WAVE
//                )
//            )
//
//            requireActivity().startService(Intent(
//                requireContext(),
//                DetailsServiceIntent::class.java
//            )
//                .apply {
//                    putExtra(BUNDLE_CITY_KEY, weatherLocal.city)
//                })

            val client = OkHttpClient()
            val builder = Request.Builder()
            builder.addHeader(YANDEX_API_KEY, BuildConfig.WEATHER_API_KEY)
            builder.url("https://api.weather.yandex.ru/v2/informers?lat=${weatherLocal.city.latitude}&lon=${weatherLocal.city.longitude}")
            val request: Request = builder.build()
            val call: Call = client.newCall(request)
            Thread {
                val response = call.execute()
                if (response.code in 200..299) {
                    response.body?.let {
                        val responseString = it.string()
                        val weatherDTO = Gson().fromJson(responseString, WeatherDTO::class.java)
                        weatherLocal.feelsLike = weatherDTO.fact.feelsLike
                        weatherLocal.temperature = weatherDTO.fact.temp
                        requireActivity().runOnUiThread {
                            renderData(weatherLocal)
                        }
                        Log.d("My_Log", "${responseString}")
                    }
                }
            }.start()
        }
    }

    private fun bindWeatherLocalWithWeatherDTO(
        weatherLocal: Weather,
        weatherDTO: WeatherDTO
    ) {
        renderData(weatherLocal.apply {
            weatherLocal.feelsLike = weatherDTO.fact.feelsLike
            weatherLocal.temperature = weatherDTO.fact.temp
        })
    }

    private fun renderData(weather: Weather) {
        with(binding) {
            cityName.text = weather.city.name
            temperatureValue.text = weather.temperature.toString()
            feelsLikeLabel.text = weather.feelsLike.toString()
            cityCoordinates.text =
                "Широта:  ${weather.city.latitude}\nДолгота: ${weather.city.longitude}"
        }
    }

    companion object {
        const val BUNDLE_WEATHER_EXTRA = "unique name"

        fun newInstance(weather: Weather) = DetailsFragment().also { fr ->
            fr.arguments = Bundle().apply {
                putParcelable(BUNDLE_WEATHER_EXTRA, weather)
            }
        }
    }
}