package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.Weather
import java.io.IOException

fun interface RepositoryLocationToWeather {
    fun getWeather(lat: Double, lon: Double, callback: TopCallback)
}

fun interface RepositoryWeatherAddable {
    fun addWeather(weather: Weather)
}

interface TopCallback {
    fun onResponse(weather: Weather)
    fun onFailure(e: IOException)
}

fun interface RepositorySingle {
    fun getWeather(lat: Double, lin: Double): Weather
}

fun interface RepositoryCitiesList {
    fun getListCities(location: CityLocation): List<Weather>
}

sealed class CityLocation {
    object Russia : CityLocation()
    object Europe : CityLocation()
}