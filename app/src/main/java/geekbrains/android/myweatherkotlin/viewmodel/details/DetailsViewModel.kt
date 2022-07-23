package geekbrains.android.myweatherkotlin.viewmodel.details

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import geekbrains.android.myweatherkotlin.MyApp
import geekbrains.android.myweatherkotlin.domain.City
import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.model.*
import geekbrains.android.myweatherkotlin.model.retrofit.RepositoryRetrofitImpl
import java.io.IOException

class DetailsViewModel(private val liveData: MutableLiveData<DetailsFragmentAppState> = MutableLiveData<DetailsFragmentAppState>()) :
    ViewModel() {

    lateinit var repositoryLocationToWeather: RepositoryWeatherByCity
    lateinit var repositoryWeatherAddable: RepositoryWeatherSave

    fun getLiveData(): MutableLiveData<DetailsFragmentAppState> {
        choiceRepository()
        return liveData
    }

    private fun choiceRepository() {
        val sp = MyApp.getMyApp().getSharedPreferences("key fdfg", Context.MODE_PRIVATE)
        if (isConnection()) {
            repositoryLocationToWeather = when (2) {
                1 -> {
                    RepositoryOkHttpImpl()
                }
                2 -> {
                    RepositoryRetrofitImpl()
                }
                3 -> {
                    RepositoryWeatherLoaderImpl()
                }
                4 -> {
                    RepositoryRoomImpl()
                }
                else -> {
                    RepositoryLocalImpl()
                }
            }
            repositoryWeatherAddable = when (0) {
                1 -> {
                    RepositoryRoomImpl()
                }
                else -> {
                    RepositoryRoomImpl()
                }
            }
        } else {
            repositoryLocationToWeather = when (1) {
                1 -> {
                    RepositoryRoomImpl()
                }
                2 -> {
                    RepositoryLocalImpl()
                }
                else -> {
                    RepositoryLocalImpl()
                }
            }
            repositoryWeatherAddable = when (0) {
                1 -> {
                    RepositoryRoomImpl()
                }
                else -> {
                    RepositoryRoomImpl()
                }
            }
        }
    }

    fun getWeather(city: City) {
        liveData.value = DetailsFragmentAppState.Loading
        repositoryLocationToWeather.getWeather(city, callback)

    }

    val callback = object : TopCallback {
        override fun onResponse(weather: Weather) {
            if (isConnection()) repositoryWeatherAddable.addWeather(weather)
            liveData.postValue(DetailsFragmentAppState.Success(weather))

        }

        override fun onFailure(e: IOException) {
            liveData.postValue(DetailsFragmentAppState.Error(e))
        }
    }

    private fun isConnection(): Boolean {
        return true
    }

    override fun onCleared() {
        super.onCleared()
    }
}