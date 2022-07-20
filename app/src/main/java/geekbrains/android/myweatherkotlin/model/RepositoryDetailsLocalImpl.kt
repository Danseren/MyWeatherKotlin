package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.domain.getDefaultCity
import geekbrains.android.myweatherkotlin.domain.getEuropeCities
import geekbrains.android.myweatherkotlin.domain.getRussianCities
import geekbrains.android.myweatherkotlin.model.dto.Fact
import geekbrains.android.myweatherkotlin.model.dto.WeatherDTO

class RepositoryDetailsLocalImpl : RepositoryLocationToWeather {
    override fun getWeather(lat: Double, lon: Double, callback: TopCallback) {
        val list = getEuropeCities().toMutableList()
        list.addAll(getRussianCities())
        val response = list.filter { it.city.latitude == lat && it.city.longitude == lon }
        callback.onResponse(response.first())
    }


}