package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.MyApp
import geekbrains.android.myweatherkotlin.domain.City
import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.model.room.WeatherEntity

class RepositoryRoomImpl : RepositoryLocationToWeather, RepositoryWeatherAddable {
    override fun getWeather(weather: Weather, callback: TopCallback) {
        callback.onResponse(
            MyApp.getWeatherDatabase().weatherDao()
                .getWeatherByLocation(weather.city.latitude, weather.city.longitude).let {
                    convertHistoryEntityToWeather(it).last()
                })
    }

    private fun convertHistoryEntityToWeather(entityList: List<WeatherEntity>): List<Weather> {
        return entityList.map {
            Weather(City(it.name + "LOAD ", it.lat, it.lon), it.temperature, it.feelsLike)
        }
    }

    override fun addWeather(weather: Weather) {
        MyApp.getWeatherDatabase().weatherDao().insertRoom(convertWeatherToEntity(weather))
    }

    private fun convertWeatherToEntity(weather: Weather): WeatherEntity {
        return WeatherEntity(
            0,
            weather.city.name + "SAVE ",
            weather.city.longitude,
            weather.city.longitude,
            weather.temperature,
            weather.feelsLike
        )
    }

}