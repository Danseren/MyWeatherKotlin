package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO
import java.io.IOException

fun interface RepositoryDetails {
    fun getWeather(lat: Double, lon: Double, callback: TopCallback)
}

interface TopCallback {
    fun onResponse(weatherDTO: WeatherDTO)
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