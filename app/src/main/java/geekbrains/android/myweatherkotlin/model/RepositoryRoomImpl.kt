package geekbrains.android.myweatherkotlin.model

import geekbrains.android.myweatherkotlin.MyApp
import geekbrains.android.myweatherkotlin.domain.City
import geekbrains.android.myweatherkotlin.domain.Weather
import geekbrains.android.myweatherkotlin.model.room.WeatherEntity

class RepositoryRoomImpl : RepositoryWeatherByCity, RepositoryWeatherSave, RepositoryWeatherAvailable {
    override fun getWeather(city: City, callback: TopCallback) {
        Thread {
            callback.onResponse(
                MyApp.getWeatherDatabase().weatherDao()
                    .getWeatherByLocation(city.latitude, city.longitude).let {
                        convertHistoryEntityToWeather(it).last()
                    })
        }.start()

    }

    override fun addWeather(weather: Weather) {
        Thread {
            MyApp.getWeatherDatabase().weatherDao().insertRoom(convertWeatherToEntity(weather))
        }.start()

    }

    override fun getWeatherAll(callback: CommonListWeatherCallback) {
        Thread {
            callback.onResponse(
                convertHistoryEntityToWeather(
                    MyApp.getWeatherDatabase().weatherDao().getWeatherAll()
                )
            )
        }.start()

    }

    private fun convertHistoryEntityToWeather(entityList: List<WeatherEntity>): List<Weather> {
        return entityList.map {
            Weather(City(it.name, it.lat, it.lon), it.temperature, it.feelsLike)
        }
    }

    private fun convertWeatherToEntity(weather: Weather): WeatherEntity {
        return WeatherEntity(
            0,
            weather.city.name,
            weather.city.longitude,
            weather.city.longitude,
            weather.temperature,
            weather.feelsLike
        )
    }
}