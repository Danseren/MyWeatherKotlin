package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.Weather

interface Repository {
    fun getListWeather(): List<Weather>
    fun getWeather(latitude: Double, longitude: Double): Weather
}