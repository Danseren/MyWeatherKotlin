package geekbrains.android.myweatherkotlin.viewmodel.details

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbrains.android.myweatherkotlin.model.*
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import java.io.IOException

class DetailsViewModel(private val liveData: MutableLiveData<DetailsFragmentAppState> = MutableLiveData<DetailsFragmentAppState>()) :
    ViewModel() {

    lateinit var repository: RepositoryDetails

    fun getLiveData(): MutableLiveData<DetailsFragmentAppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        repository = when (5) {
            1 -> {
                RepositoryDetailsOkHttpImpl()
            }
            2 -> {
                RepositoryDetailsRetrofitImpl()
            }
            3 -> {
                RepositoryDetailsWeatherLoaderImpl()
            }
            else -> {
                RepositoryDetailsLocalImpl()
            }

        }
    }

    fun getWeather(lat: Double, lon: Double) {
        choiceRepository()
        liveData.value = DetailsFragmentAppState.Loading
        repository.getWeather(lat, lon, callback)

    }

    val callback = object : TopCallback {
        override fun onResponse(weatherDTO: WeatherDTO) {
            liveData.postValue(DetailsFragmentAppState.Success(weatherDTO))
            Handler(Looper.getMainLooper()).post {

            }
        }

        override fun onFailure(e: IOException) {
            liveData.postValue(DetailsFragmentAppState.Error(e))
        }

    }

    private fun isConnection(): Boolean {
        return false
    }

    override fun onCleared() {
        super.onCleared()
    }
}