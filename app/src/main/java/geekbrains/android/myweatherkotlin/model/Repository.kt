package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.City
import geekbrains.android.myweatherkotlin.domain.Weather
import java.io.IOException

fun interface RepositoryWeatherByCity {
    fun getWeather(city: City, callback: TopCallback)
}

fun interface RepositoryWeatherSave {
    fun addWeather(weather: Weather)
}

fun interface RepositoryWeatherAvailable {
    fun getWeatherAll(callback: CommonListWeatherCallback)
}

interface TopCallback {
    fun onResponse(weather: Weather)
    fun onFailure(e: IOException)
}

interface CommonListWeatherCallback {
    fun onResponse(weather: List<Weather>)
    fun onFailure(e: IOException)
}

fun interface RepositoryCitiesList {
    fun getListCities(location: CityLocation): List<Weather>
}

sealed class CityLocation {
    object Russia : CityLocation()
    object Europe : CityLocation()
}