package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.Weather

fun interface RepositoryDetails {
    fun getWeather(lat: Double, lon: Double): Weather
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