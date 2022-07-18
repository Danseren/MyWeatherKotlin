package geekbrains.android.myweatherkotlin.view.view.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import geekbrains.android.myweatherkotlin.R
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

    lateinit var weatherLocal: Weather

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
        weather?.let { weatherLocal ->
            this.weatherLocal = weatherLocal
            viewModel.getWeather(weatherLocal.city.latitude, weatherLocal.city.longitude)
            viewModel.getLiveData().observe(viewLifecycleOwner) {
                renderData(it)
            }
        }
    }

    private fun renderData(detailsFragmentAppState: DetailsFragmentAppState) {

        when (detailsFragmentAppState) {
            is DetailsFragmentAppState.Error -> {}
            DetailsFragmentAppState.Loading -> {}
            is DetailsFragmentAppState.Success -> {
                with(binding) {
                    cityName.text = weatherLocal.city.name
                    temperatureValue.text = detailsFragmentAppState.weatherData.fact.temp.toString()
                    feelsLikeLabel.text =
                        detailsFragmentAppState.weatherData.fact.feelsLike.toString()
                    cityCoordinates.text =
                        "Широта:  ${weatherLocal.city.latitude}\nДолгота: ${weatherLocal.city.longitude}"
                    icon.load("https://i.pinimg.com/originals/de/1f/6f/de1f6f936d497684c4a023dcde8576cc.jpg\n") {
                        error(R.drawable.ic_russia)
                        placeholder(R.drawable.ic_launcher_background)
                        transformations(CircleCropTransformation())
                    }

//                    Glide.with(this.root)
//                        .load("https://freepngimg.com/thumb/city/36275-3-city-hd.png").into(icon)

//                    Picasso.get().load("https://freepngimg.com/thumb/city/36275-3-city-hd.png")
//                        .into(icon)
                }
            }
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        //LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(receiver)
    }
}