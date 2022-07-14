package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.Weather

fun interface RepositorySingle {
    fun getWeather(lat: Double, lin: Double): Weather
}

fun interface RepositoryMulti {
    fun getListWeather(location: CityLocation): List<Weather>
}

sealed class CityLocation {
    object Russia : CityLocation()
    object Europe : CityLocation()
}