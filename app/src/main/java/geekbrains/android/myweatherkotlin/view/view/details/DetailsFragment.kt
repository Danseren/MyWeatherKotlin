package geekbrains.android.myweatherkotlin.view.view.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import geekbrains.android.myweatherkotlin.databinding.FragmentDetailsBinding
import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.viewmodel.details.DetailsFragmentAppState
import geekbrains.android.myweatherkotlin.viewmodel.details.DetailsViewModel

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() {
            return _binding!!
        }

    //lateinit var weatherLocal: Weather

    val viewModel by lazy {
        ViewModelProvider(this).get(DetailsViewModel::class.java)
    }

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
        weather?.let {
            viewModel.getLiveData().observe(viewLifecycleOwner) {
                renderData(it)
            }
            viewModel.getWeather(it.city)
        }
    }

    private fun renderData(detailsFragmentAppState: DetailsFragmentAppState) {

        when (detailsFragmentAppState) {
            is DetailsFragmentAppState.Error -> {}
            DetailsFragmentAppState.Loading -> {}
            is DetailsFragmentAppState.Success -> {
                with(binding) {
                    val weather = detailsFragmentAppState.weatherData
                    cityName.text = weather.city.name
                    temperatureValue.text =
                        weather.temperature.toString()
                    feelsLikeLabel.text =
                        weather.feelsLike.toString()
                    cityCoordinates.text =
                        "Широта:  ${weather.city.latitude}\nДолгота: ${weather.city.longitude}"

                    icon.loadUrl("https://yastatic.net/weather/i/icons/funky/dark/${detailsFragmentAppState.weatherData.icon}.svg")
                }
            }
        }
    }

    fun ImageView.loadUrl(url: String) {
        val imageLoader = ImageLoader.Builder(this.context)
            .components {
                add(SvgDecoder.Factory())
            }
            .build()

        val request = ImageRequest.Builder(this.context)
            .crossfade(true)
            .crossfade(500)
            .data(url)
            .target(this)
            .build()

        imageLoader.enqueue(request)
    }

    companion object {
        const val BUNDLE_WEATHER_EXTRA = "unique name"

        fun newInstance(weather: Weather) = DetailsFragment().also { fr ->
            fr.arguments = Bundle().apply {
                putParcelable(BUNDLE_WEATHER_EXTRA, weather)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }
}