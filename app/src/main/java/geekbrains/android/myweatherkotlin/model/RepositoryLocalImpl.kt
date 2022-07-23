package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.City
import geekbrains.android.myweatherkotlin.domain.getEuropeCities
import geekbrains.android.myweatherkotlin.domain.getRussianCities

class RepositoryLocalImpl : RepositoryWeatherByCity {
    override fun getWeather(city: City, callback: TopCallback) {
        val list = getEuropeCities().toMutableList()
        list.addAll(getRussianCities())
        val response =
            list.filter { it.city.latitude == city.latitude && it.city.longitude == city.longitude }
        callback.onResponse(response.first())
    }
}